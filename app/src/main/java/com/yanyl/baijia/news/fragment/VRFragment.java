package com.yanyl.baijia.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.YanylApp;
import com.yanyl.baijia.news.adapter.NewsAdapter;
import com.yanyl.baijia.news.atys.DetailNewsActivity;
import com.yanyl.baijia.news.bean.XinwenBean;
import com.yanyl.baijia.news.url.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yanyl on 2016/10/22.
 */
public class VRFragment extends BaseFragment {
    private List<XinwenBean> lists=new ArrayList<XinwenBean>();
    private XinwenBean xinwenBean;


    private PullToRefreshListView pullToRefreshListView;
    private NewsAdapter adapter;
    private int page=1;

    // private StringRequest request;


    public VRFragment(){}
    private static VRFragment vrFragment=null;
    public static VRFragment getInstance(){
        if (vrFragment==null){
            synchronized (VRFragment.class){
                if (vrFragment==null){
                    vrFragment=new VRFragment();
                }
            }
        }
        return vrFragment;
    }
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.common_fragment,container,false);


        lists=new ArrayList<XinwenBean>();
        pullToRefreshListView= (PullToRefreshListView) view.findViewById(R.id.listview);
        adapter=new NewsAdapter(lists,getContext());

        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                xinwenBean=adapter.getItem(position);
                String url=xinwenBean.getContentUrl();

                Intent intent =new Intent(getActivity(),DetailNewsActivity.class);
                intent.putExtra("detail",url);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right,
                        R.anim.out_to_left);
            }
        });
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                adapter.clear();
                page=1;
                loadData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                loadData();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }
    public  void loadData() {

        StringRequest request = new StringRequest(
                Request.Method.POST,
               Url.BAIJIA_VR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject=new JSONObject(response);

                            JSONObject dataJsonObject= jsonObject.getJSONObject("data");

                            JSONArray jsonArray=dataJsonObject.getJSONArray("list");


                            for (int i=0;i<jsonArray.length();i++){
                                String id=jsonArray.getJSONObject(i).getString("ID");
                                String time=jsonArray.getJSONObject(i).getString("m_create_time");
                                String title=jsonArray.getJSONObject(i).getString("m_title");
                                String imageUrl=jsonArray.getJSONObject(i).getString("m_image_url");
                                String contentUrl=jsonArray.getJSONObject(i).getString("m_display_url");
                                lists.add(new XinwenBean(id,time,title,imageUrl,contentUrl));
                            }
                            adapter.addAll(lists);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                null
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                map.put("p", page+"");
                map.put("size", "10");
                map.put("tab", "dianshang");
                return map;
            }


        };
        request.setTag("daishang");
        YanylApp.getApp().getQueue().add(request);
//        加载完数据后，隐藏刷新条
        pullToRefreshListView.onRefreshComplete();

    }

    @Override
    public void onDestroy() {
        YanylApp.getApp().getQueue().cancelAll("daishang");
        super.onDestroy();
    }


}
