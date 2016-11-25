package com.yanyl.baijia.news.atys;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.bean.User;
import com.yanyl.baijia.news.util.Util;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * @author yanyl
 * Created by yanyl on 2016/10/19.
 * 普通注册界面
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener{

    //用户名输入框
    private EditText mEdtUser;
    //密码输入框
    private EditText mEdtPass;
    //确认密码输入框
    private EditText mEdtPwd;

    private TextInputLayout mLayoutUser;
    private TextInputLayout mLayoutpass;
    private TextInputLayout mLayoutpwd;

    //登录按钮
    private Button mzhuceButon;
    //关闭功能
    private ImageView mCloseImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //初始化控件
        initView();
    }

    private void initView() {

        mEdtUser= (EditText) findViewById(R.id.id_cz_user);
        mEdtPass= (EditText) findViewById(R.id.id_cz_password);
        mEdtPwd= (EditText) findViewById(R.id.id_cz_pwd);

        mLayoutUser= (TextInputLayout) findViewById(R.id.layout_name);
        mLayoutpass= (TextInputLayout) findViewById(R.id.layout_new_pass);
        mLayoutpwd= (TextInputLayout) findViewById(R.id.layout_new_pwd);
        mzhuceButon= (Button) findViewById(R.id.id_zhuce);
        mCloseImageView= (ImageView) findViewById(R.id.id_close);


        //添加输入文本改变的监听事件
        mEdtUser.addTextChangedListener(new MyTextWatcher(mEdtUser));
        mEdtPass.addTextChangedListener(new MyTextWatcher(mEdtPass));
        mEdtPwd.addTextChangedListener(new MyTextWatcher(mEdtPwd));

        mzhuceButon.setOnClickListener(this);
        mCloseImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //关闭窗口
            case R.id.id_close:
                Util.backIntent(RegisterActivity.this);
                break;
            case R.id.id_zhuce:
                loadingData();
                break;
            default:
                break;
        }
    }

    private void loadingData() {
        if (isPwdValid()&&isPasswordValid()&&isNameValid()){
            if (isExistance()){
                Util.showToast(RegisterActivity.this,"该账号已经注册,请登录");
            }else{
                //判断密码与确认密码是否相同
                if(mEdtPass.getText().toString().trim().equals(
                        mEdtPwd.getText().toString().trim())){
                    //账户数据保存数据库内
                    saveToDB();
                    Util.showToast(RegisterActivity.this,"注册完成，请登录");
                }else{
                    Util.showToast(RegisterActivity.this,"两次密码不同");
                }
            }

        }else{
            Util.showToast(RegisterActivity.this,"输入用户名或密码有误，请检查");
        }
    }

    private void saveToDB() {
        User user=new User();
        user.setName(mEdtUser.getText().toString().trim());
        user.setPwd(mEdtPwd.getText().toString().trim());
        user.setPassword(mEdtPass.getText().toString().trim());
        user.setTime(Util.getTime());
        user.save();
    }

    //判断账号是否应注册
    private boolean isExistance(){
        //从数据库中查找
        List<User> userList= DataSupport
                .where("name=?",mEdtUser.getText().toString().trim())
                .find(User.class);
        if (userList.size()!=0){
            return true;
        }
        return false;
    }

    //动态监听输入过程
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.id_cz_user:
                    //判断输入的用户名是否可用
                    isNameValid();
                    break;
                case R.id.id_cz_password:
                    //判断输入的密码是否可用
                    isPasswordValid();
                    break;
                case R.id.id_cz_pwd:
                    //输入的确认密码是否可用
                    isPwdValid();
                    break;
            }

        }
    }
    private boolean isPwdValid() {
        if (mEdtPwd.getText().toString().trim().equals("")||
                mEdtPwd.getText().toString().trim().isEmpty()||
                mEdtPwd.getText().toString().trim().length()>18&&
                        mEdtPwd.getText().toString().trim().length()<6){
            mEdtPwd.setError("输入确认密码");
            mEdtPwd.requestFocus();
            return false;
        }
        mLayoutpwd.setErrorEnabled(true);
        return true;
    }

    private boolean isPasswordValid() {
        if (mEdtPass.getText().toString().trim().equals("")||
                mEdtPass.getText().toString().trim().isEmpty()||
                mEdtPass.getText().toString().trim().length()>18&&
                        mEdtPass.getText().toString().trim().length()<6){
            mEdtPass.setError("输入密码");
            mEdtPass.requestFocus();
            return false;
        }
        mLayoutpass.setErrorEnabled(true);
        return true;
    }

    private boolean isNameValid() {
        if (mEdtUser.getText().toString().trim().equals("")||
                mEdtUser.getText().toString().trim().isEmpty()){
            mEdtUser.setError("输入用户名");
            mEdtUser.requestFocus();
            return false;
        }
        mLayoutUser.setErrorEnabled(true);
        return true;
    }
}