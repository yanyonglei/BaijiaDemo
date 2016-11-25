package com.yanyl.baijia.news.atys;

import android.content.ContentValues;
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

/**
 * Created by yanyl on 2016/11/15.
 * 重置密码窗体
 */
public class ChongzhiPaw extends BaseActivity implements View.OnClickListener {


    //用户名输入框
    private EditText mEdtUser;
    //密码输入框
    private EditText mEdtPass;
    //确认密码输入框
    private EditText mEdtPwd;

    //文字 提示框
    private TextInputLayout mLayoutUser;
    private TextInputLayout mLayoutpass;
    private TextInputLayout mLayoutpwd;

    private ImageView mImgClose;

    private Button mBtnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cz_paw);
        initViews();
    }

    private void initViews() {
        mEdtUser= (EditText) findViewById(R.id.id_cz_user);
        mEdtPass= (EditText) findViewById(R.id.id_cz_password);
        mEdtPwd= (EditText) findViewById(R.id.id_cz_pwd);
        mImgClose= (ImageView) findViewById(R.id.id_close);

        mLayoutUser= (TextInputLayout) findViewById(R.id.layout_name);
        mLayoutpass= (TextInputLayout) findViewById(R.id.layout_new_pass);
        mLayoutpwd= (TextInputLayout) findViewById(R.id.layout_new_pwd);


        mEdtUser.addTextChangedListener(new MyTextWatcher(mEdtUser));
        mEdtPass.addTextChangedListener(new MyTextWatcher(mEdtPass));
        mEdtPwd.addTextChangedListener(new MyTextWatcher(mEdtPwd));

        mBtnModify= (Button) findViewById(R.id.id_cz_modify);
        mImgClose.setOnClickListener(this);
        mBtnModify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //修改密码
            case R.id.id_cz_modify:
                updatePassword();
                break;
            //关闭窗体
            case R.id.id_close:
                Util.backIntent(ChongzhiPaw.this);
                break;
            default:
                break;
        }
    }

    //修改密码
    private void updatePassword() {
        if (isPwdValid()&&isPasswordValid()&&isNameValid()){
            User user= DataSupport.where("name=?",mEdtUser.getText().toString().trim()).findFirst(User.class);
            if (user==null){
                Util.showToast(ChongzhiPaw.this,"用户名不存在，请注册");
            }else if(mEdtPass.getText().toString().trim().equals(mEdtPwd.getText().toString().trim())){
                ContentValues values=new ContentValues();
                values.put("name",mEdtUser.getText().toString().trim());
                values.put("password",mEdtPass.getText().toString().trim());
                values.put("pwd",mEdtPwd.getText().toString().trim());
                values.put("time",Util.getTime());
                //数据库数据更新
                DataSupport.updateAll(User.class,values,"name=?",user.getName());
                Util.showToast(ChongzhiPaw.this,"密码重置完成，请登录");
            }else{
                Util.showToast(ChongzhiPaw.this,"两次密码不同");
            }
        }else{
            Util.showToast(ChongzhiPaw.this,"输入用户名或密码有误，请检查");
        }
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
                    isNameValid();
                    break;
                case R.id.id_cz_password:
                  isPasswordValid();
                    break;
                case R.id.id_cz_pwd:
                    isPwdValid();
                    break;

            }

        }
    }

    //判断确认密码是否可用
    private boolean isPwdValid() {
        if (mEdtPwd.getText().toString().trim().equals("")||
                mEdtPwd.getText().toString().trim().isEmpty()||
                mEdtPwd.getText().toString().trim().length()>18&&
                        mEdtPwd.getText().toString().trim().length()<6){
            mEdtPwd.setError("输入密码");
            mEdtPwd.requestFocus();
            return false;
        }
        mLayoutpwd.setErrorEnabled(true);
        return true;
    }
    //判断确认密码是否可用
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
    //判断用户名是否可用
    private boolean isNameValid() {
        if (mEdtUser.getText().toString().trim().equals("")||
                mEdtUser.getText().toString().trim().isEmpty()){
            mEdtUser.setError("输如用户名");
            mEdtUser.requestFocus();
            return false;
        }
        mLayoutUser.setErrorEnabled(true);
        return true;
    }
}
