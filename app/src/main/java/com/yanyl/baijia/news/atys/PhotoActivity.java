package com.yanyl.baijia.news.atys;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.adapter.ImageAdapter;
import com.yanyl.baijia.news.bean.ImageBean;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;


import java.util.ArrayList;

import java.util.List;


/**
 * Created by yanyl on 2016/10/25.
 */
public class PhotoActivity extends BaseActivity {

    public static final String PATH = "url";
    public static final String ID = "id";

    private PullToRefreshListView mListView;

    private ImageAdapter adapter;

    public List<ImageBean> lists;
    private ImageBean imageBean;
    //网络路径
    private String path;

    //异步任务

    private int page = 1;
    private int id = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        path = getIntent().getStringExtra(PATH);


        id=getIntent().getIntExtra(ID,0);

        lists = new ArrayList<ImageBean>();

        mListView = (PullToRefreshListView) findViewById(R.id.listview);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        adapter = new ImageAdapter(lists, PhotoActivity.this,id);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                imageBean = adapter.getItem(position);
                Intent intent=new Intent(PhotoActivity.this,DownActivity.class);
                intent.putExtra("detail", imageBean.getImg());

                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);


            }
        });
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                adapter.clear();
                page = 1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                loadData();
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        loadData();

    }

    public void loadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(path, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                try {
                    //字符串的分割
                    String json = response.substring(15, response.length() - 1);
                    JSONObject jsonObject = new JSONObject(json);
                    final JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String title = jsonArray.getJSONObject(i).getString("vid");
                        String imgurl = jsonArray.getJSONObject(i).getString("img");
                        String content = jsonArray.getJSONObject(i).getString("url");
                        JSONObject object=jsonArray.getJSONObject(i).getJSONObject("comment");
                        String time=object.getString("createTime");


                        lists.add(new ImageBean(title,imgurl,content,time));
                        //数据信息保存到数据库


                       /* imageBean=new ImageBean();
                        imageBean.setImg(imgurl);
                        imageBean.setTitle(title);
                        imageBean.setTime(time);
                        imageBean.setContent(content);
                        imageBean.saveThrows();*/
                    }
                    adapter.addAll(lists);
                    DataSupport.saveAll(lists);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

                    @Override
                    public void onFailure(Throwable error) {
                        super.onFailure(error);
                    }
                }
        );
        mListView.onRefreshComplete();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}





