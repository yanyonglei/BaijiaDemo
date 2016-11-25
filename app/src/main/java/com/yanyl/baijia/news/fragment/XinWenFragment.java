package com.yanyl.baijia.news.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.action.RotateUpTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yanyl on 2016/10/19.
 */
public class XinWenFragment extends BaseFragment {

    private List<String> mTitles=new ArrayList<String>();


    private TabLayout mTabLaout;
    private ViewPager  mViewPager;
    private List<Fragment> fragments=new ArrayList<Fragment>();

    private FirstFragment     firstFragment;
    private JuTouFragment     juTouFragment;
    private RenwuFragment      renwuFragment;
    private DianshangFragment  dianshangFragment;
    private ChuangTouFragment   chuangTouFragment;
    private VRFragment   vrFragment;
    private ZhiNengFragment   zhiNengFragment;

	public XinWenFragment(){}
	private static XinWenFragment xinwenFragment=null;

	public static XinWenFragment getInstance(){
			if (xinwenFragment==null){
				synchronized(XinWenFragment.class){
					if (xinwenFragment==null)
					{
						xinwenFragment=new XinWenFragment();

					}
				}
			}
			return xinwenFragment;

		}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.xinwen_fragment,container,false);


        mTitles.add("首页");
        mTitles.add("巨头");
        mTitles.add("人物");
        mTitles.add("电商");
        mTitles.add("创投");
        mTitles.add("VR");
        mTitles.add("硬件智能");
        mTitles.add("科技");

        initFragment();
        mTabLaout= (TabLayout) view.findViewById(R.id.tab_layout);
        mViewPager= (ViewPager) view.findViewById(R.id.xinwen_viewpager);
        mTabLaout.setTabTextColors(Color.parseColor("#000000"),Color.parseColor("#ff0000"));
        mTabLaout.setSelectedTabIndicatorColor(Color.parseColor("#ffff99"));

        mViewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }

        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
               mViewPager.setCurrentItem(0);

        //页面旋转
        mViewPager.setPageTransformer(true, new RotateUpTransformer());
        mTabLaout.setupWithViewPager(mViewPager);
        return view;
    }

    private void initFragment() {

        firstFragment= FirstFragment.getInstance();
        juTouFragment= JuTouFragment.getInstance();
        renwuFragment= RenwuFragment.getInstance();
        dianshangFragment= DianshangFragment.getInstance();
        chuangTouFragment= ChuangTouFragment.getInstance();
        vrFragment= VRFragment.getInstance();
        zhiNengFragment= ZhiNengFragment.getInstance();

        fragments.add(firstFragment);
        fragments.add(juTouFragment);
        fragments.add(renwuFragment);
        fragments.add(dianshangFragment);
        fragments.add(chuangTouFragment);
        fragments.add(vrFragment);
        fragments.add(zhiNengFragment);
    }


}
