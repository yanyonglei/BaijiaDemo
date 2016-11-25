package com.yanyl.baijia.news.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.YanylApp;
import com.yanyl.baijia.news.adapter.NewsAdapter;
import com.yanyl.baijia.news.atys.DetailNewsActivity;
import com.yanyl.baijia.news.bean.ImageBean;
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
public class FirstFragment extends BaseFragment {

    private ImageBean imageBean;

    private List<XinwenBean>   lists=new ArrayList<XinwenBean>();
    //新闻
    private XinwenBean xinwenBean;

    //自动刷新listview
    private PullToRefreshListView  pullToRefreshListView;
    private NewsAdapter adapter;
    private int page=1;

    private List<ImageView> mListImg;
    private ViewPager mViewPager;

    private int[] imgIds=new int[]{R.drawable.img_baijia,R.drawable.img1_baijia,
            R.drawable.img2_baijia,R.drawable.img3_baijia};
  //  private RequestQueue queue;


    private LinearLayout mLlPoint;

    /**
     * 上一个页面的位置
     */
    protected int lastPosition;



    public FirstFragment(){};
    private static FirstFragment ftFragment=null;
    public static FirstFragment getInstance(){
        if (ftFragment==null){
            synchronized (FirstFragment.class){
                if (ftFragment==null){
                    ftFragment=new FirstFragment();
                }
            }
        }
        return ftFragment;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.first_fragment,container,false);

        mLlPoint= (LinearLayout) view.findViewById(R.id.id_point);
        mListImg=new ArrayList<ImageView>();
        for (int i=0;i<imgIds.length;i++){
            //初始化图片资源
            ImageView imageView=new ImageView(getContext());

            imageView.setBackgroundResource(imgIds[i]);
            mListImg.add(imageView);

            //添加指示点
            ImageView point =new ImageView(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.rightMargin = 25;
            point.setLayoutParams(params);
            point.setBackgroundResource(R.drawable.point_bg);
            if(i==0){
                point.setEnabled(true);
            }else{
                point.setEnabled(false);
            }
            mLlPoint.addView(point);
        }
        mViewPager= (ViewPager) view.findViewById(R.id.id_viewpager);


        mViewPager.setVisibility(View.VISIBLE);
        mViewPager.setAdapter(new ImgePageAadpeter());
        mViewPager.setCurrentItem(Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2%mListImg.size())) ;
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                position = position%mListImg.size();

                //把当前点enbale 为true
                mLlPoint.getChildAt(position).setEnabled(true);
                //把上一个点设为false
                mLlPoint.getChildAt(lastPosition).setEnabled(false);
                lastPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        isRunning = true;
	    handler.sendEmptyMessageDelayed(0,3*1000);

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

    /**
     * 判断是否自动滚动
     */
    private boolean isRunning = false;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {

            //让viewPager 滑动到下一页
            mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            if(isRunning){
                handler.sendEmptyMessageDelayed(0,3*1000);
            }
        }
    };





    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }
    public  void loadData() {
        StringRequest request = new StringRequest(
                Request.Method.POST,
                Url.BAIJIA_FIRST,
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
                map.put("tab", "first");
                return map;
            }


        };
        request.setTag("first");
        YanylApp.getApp().getQueue().add(request);
//        加载完数据后，隐藏刷新条
        pullToRefreshListView.onRefreshComplete();
    }

    @Override
    public void onDestroy() {
        YanylApp.getApp().getQueue().cancelAll("first");
        isRunning=false;
        super.onDestroy();
    }


    class ImgePageAadpeter extends PagerAdapter{

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override

        public Object instantiateItem(ViewGroup container, int position) {

            // 给 container 添加一个view
            container.addView(mListImg.get(position%mListImg.size()));
            //返回一个和该view相对的object
            return mListImg.get(position%mListImg.size());
        }

        @Override


        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }
}
