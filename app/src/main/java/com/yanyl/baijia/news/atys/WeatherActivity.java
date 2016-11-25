package com.yanyl.baijia.news.atys;


import android.os.Bundle;

import android.support.design.widget.TextInputLayout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.mikepenz.itemanimators.SlideDownAlphaAnimator;
import com.mob.mobapi.MobAPI;
import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.adapter.DividerItemDecoration;
import com.yanyl.baijia.news.adapter.WeatherAdapter;
import com.yanyl.baijia.news.bean.TianQiBean;
import com.yanyl.baijia.news.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yanyl on 2016/11/17.
 */
public class WeatherActivity extends BaseActivity  {


    //天气appKey 来自mob.com
    private static final String APP_KEY="192164865837c";
    //省份
    private TextInputLayout mTilPro;
    //城市
    private TextInputLayout mTilCity;
    //日期
    private TextInputLayout mTilDate;
    //省份输入框
    private EditText mEtProvince;
    //城市名
    private EditText mEtCity;
    //日期
    private EditText mEtDate;
    //查询
    private Button mBtncx;

    //天气的集合
    public  List<TianQiBean> tianQiBeanList;

    //返回
    private LinearLayout mLlayoutBack;
    private RecyclerView mRecyclerView;
    //天气适配器
    private WeatherAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        MobAPI.initSDK(this,APP_KEY);


        initViews();
    }

    private void initViews() {
        mTilPro= (TextInputLayout) findViewById(R.id.layout_province);

        mTilCity= (TextInputLayout) findViewById(R.id.layout_city);

        mTilDate= (TextInputLayout) findViewById(R.id.layout_date);

        mEtProvince= (EditText) findViewById(R.id.id_province);

        mEtCity= (EditText) findViewById(R.id.id_city);

        mEtDate= (EditText) findViewById(R.id.id_date);


        mRecyclerView= (RecyclerView) findViewById(R.id.recycleView);

        mLlayoutBack= (LinearLayout) findViewById(R.id.id_back);
        mLlayoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.backIntent(WeatherActivity.this);
            }
        });

        mBtncx= (Button) findViewById(R.id.id_cx);

        mEtProvince.addTextChangedListener(new MyTextWatcher(mEtProvince));
        mEtCity.addTextChangedListener(new MyTextWatcher(mEtCity));
        mEtDate.addTextChangedListener(new MyTextWatcher(mEtDate));


        mBtncx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeatherInformation();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager
                (this,LinearLayoutManager.VERTICAL,false));


       mRecyclerView.setItemAnimator(new SlideDownAlphaAnimator());

        //添加划分线 水平
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL_LIST));

    }
    private void getWeatherInformation() {

        if (isDateValid()&&isProvinceValid()&&isCityValid()) {
            List<TianQiBean> tianQiList = DataSupport
                    .where("date=?", mEtDate.getText().toString().trim())
                    .find(TianQiBean.class);
            //如果数据库中已经缓存了数据
            if (tianQiList.size()!=0) {
                    System.out.println("数据不为空");
                    mAdapter = new WeatherAdapter(tianQiList, WeatherActivity.this);
                    mRecyclerView.setAdapter(mAdapter);

               }else {
                    //否则 从网络获取
                    if (isCityValid() && isProvinceValid()) {
                        String url = "http://apicloud.mob.com/v1/weather/query?key=" + APP_KEY + "&city=" +
                                mEtCity.getText().toString().trim() + "&province=" +
                                mEtProvince.getText().toString().trim() + "";
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.get(url, new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(String content) {
                                if (content != null) {
                                    try {
                                        JSONObject weatherJson = new JSONObject(content);
                                        String msg = weatherJson.getString("msg");
                                        if (msg.equals("success")) {
                                            tianQiBeanList = new ArrayList<TianQiBean>();
                                            JSONArray demo = weatherJson.getJSONArray("result");
                                            String distrct = demo.getJSONObject(0).getString("distrct");
                                            String city = demo.getJSONObject(0).getString("city");
                                            JSONArray demos = demo.getJSONObject(0).getJSONArray("future");
                                            for (int i = 0; i < demos.length(); i++) {
                                                String day = null;
                                                //判断某个键值，是否
                                                if (demos.getJSONObject(i).has("dayTime")) {
                                                    day = demos.getJSONObject(i).getString("dayTime");
                                                } else {
                                                    day = "";
                                                }

                                                String date = demos.getJSONObject(i).getString("date");
                                                String temperature = demos.getJSONObject(i).
                                                        getString("temperature");
                                                String night = demos.getJSONObject(i).getString("night");
                                                String wind = demos.getJSONObject(i).getString("wind");
                                                tianQiBeanList.add(new TianQiBean
                                                        (msg, date, temperature, wind, night,
                                                                distrct, city, mEtProvince.getText().
                                                                toString().trim(), day));
                                            }

                                            mAdapter = new WeatherAdapter
                                                    (tianQiBeanList, WeatherActivity.this);
                                            mRecyclerView.setAdapter(mAdapter);
                                            //天气数据加入数据库备份
                                            DataSupport.saveAll(tianQiBeanList);
                                        } else {
                                            Util.showToast(WeatherActivity.this, content);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
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
                case R.id.id_province:
                    isProvinceValid();
                    break;
                case R.id.id_city:
                    isCityValid();
                    break;
                case R.id.id_date:
                    isDateValid();
            }
        }
    }

    private boolean isDateValid() {
        if(mEtDate.getText().toString().trim().equals("")||
                mEtDate.getText().toString().trim().isEmpty() ){
            mEtDate.setError("输入日期");
            mEtDate.requestFocus();
            return false;
        }
        mTilDate.setErrorEnabled(true);
        return true;
    }

    private boolean isCityValid() {
        if (mEtCity.getText().toString().trim().equals("")||
                mEtCity.getText().toString().trim().isEmpty()) {
            mEtCity.setError("输入城市名");
            mEtCity.requestFocus();
            return false;
        }
        mTilCity.setErrorEnabled(true);
        return true;
    }

    private boolean isProvinceValid() {
        if (mEtProvince.getText().toString().trim().equals("")||
                mEtProvince.getText().toString().trim().isEmpty()) {
            mEtProvince.setError("输入省份");
            mEtProvince.requestFocus();
            return false;
        }
        mTilPro.setErrorEnabled(true);
        return true;
    }

}