package com.yanyl.baijia.news.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanyl.baijia.news.R;

import com.yanyl.baijia.news.adapter.ColorListAdapter;
import com.yanyl.baijia.news.atys.AboutActivity;
import com.yanyl.baijia.news.atys.ContractActivity;
import com.yanyl.baijia.news.atys.FankuiActivity;
import com.yanyl.baijia.news.atys.UserLoginActivity;
import com.yanyl.baijia.news.atys.DateActivity;
import com.yanyl.baijia.news.atys.WeatherActivity;
import com.yanyl.baijia.news.util.DataClearManager;
import com.yanyl.baijia.news.util.PreferenceUtils;
import com.yanyl.baijia.news.util.ThemeUtils;
import com.yanyl.baijia.news.util.Util;


import java.util.Arrays;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by yanyl on 2016/10/19.
 */
public class SettingFragment extends BaseFragment {

    //账户管理
    private RelativeLayout mSettingRelative;
    //联系好友
    private RelativeLayout mRltContract;
    //时钟
    private RelativeLayout mRltClock;
    //模式选择 夜间模式
    private RelativeLayout mRltMode;
    //天气
    private RelativeLayout mRldayWeather;
    //版本信息
    private RelativeLayout mRltInformation;
    //意见反馈
    private RelativeLayout mRltFankui;
    //关于我们
    private RelativeLayout mRltAbout;

    //缓存
    private TextView mTvCahce;
    //清理缓存
    private ImageView mImgClear;
    private MyListener listener;

    private  ColorListAdapter mColorAdapter;

    private GridView mGridView;

    private LayoutInflater mInflater;

	public SettingFragment(){}
	private static SettingFragment settingFragment=null;

	public static SettingFragment getInstance(){
		if (settingFragment==null)
		{
			synchronized(SettingFragment.class){
				if (settingFragment==null)
				{
					settingFragment=new SettingFragment();
				}
			}
			
		}
	     return settingFragment;
	
	}


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.setting_fragment,container,false);
        listener=new MyListener();
        mSettingRelative= (RelativeLayout) view.findViewById(R.id.id_re_guanli);
        mRltContract= (RelativeLayout) view.findViewById(R.id.id_re_contract);
        mRltClock= (RelativeLayout) view.findViewById(R.id.id_re_clock);
        mRltMode= (RelativeLayout) view.findViewById(R.id.id_re_model);
        mRldayWeather= (RelativeLayout) view.findViewById(R.id.id_re_weather);
        mRltInformation= (RelativeLayout) view.findViewById(R.id.id_re_information);
        mRltFankui= (RelativeLayout) view.findViewById(R.id.id_re_fankui);
        mRltAbout= (RelativeLayout) view.findViewById(R.id.id_re_about);


        mTvCahce= (TextView) view.findViewById(R.id.tv_cache);
        try {
            mTvCahce.setText(DataClearManager.getTotalCacheSize(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mImgClear= (ImageView) view.findViewById(R.id.img_clear);
        mImgClear.setOnClickListener(listener);

        mSettingRelative.setOnClickListener(listener);
        mRltContract.setOnClickListener(listener);
        mRltClock.setOnClickListener(listener);
        mRltMode.setOnClickListener(listener);
        mRldayWeather.setOnClickListener(listener);
        mRltInformation.setOnClickListener(listener);
        mRltFankui.setOnClickListener(listener);
        mRltAbout.setOnClickListener(listener);

        return view;
    }
    private class MyListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.id_re_guanli:
                    Util.enterIntent(getActivity(), UserLoginActivity.class);
                    break;
                case R.id.id_re_contract:
                    Util.enterIntent(getActivity(), ContractActivity.class);
                    break;
                case R.id.id_re_clock:
                    Util.enterIntent(getActivity(),DateActivity.class);
                    break;
                case R.id.id_re_model:
                    //弹出Dialog 选择主题的颜色
                    showThemeChooseDialog();
                    break;
                case R.id.id_re_weather:
                    Util.enterIntent(getContext(), WeatherActivity.class);
                    break;
                case R.id.id_re_information://显示版本信息
                    final AlertDialog dialog=new AlertDialog.Builder(getContext()).create();
                    mInflater=getActivity().getLayoutInflater();
                    View view=mInflater.inflate(R.layout.dialog_information_banben,null);
                    Button mBtnYes= (Button) view.findViewById(R.id.id_yes);
                    mBtnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    Button mBtnNo= (Button) view.findViewById(R.id.id_no);
                    mBtnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setView(view);
                    dialog.show();
                    break;
                case R.id.id_re_fankui:
                    Util.enterNext(getContext(), FankuiActivity.class);
                    break;

                case R.id.img_clear:
                    DataClearManager.clearAllCache(getContext());
                    mTvCahce.setText("0KB");
                    break;
                case R.id.id_re_about:
                    Util.enterNext(getContext(),AboutActivity.class);
            }
        }
    }

    private PreferenceUtils preferenceUtils=PreferenceUtils.getInstance(getContext());

    private void showThemeChooseDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("更换主题");
        //初始化主题的颜色
        Integer[] res = new Integer[]{R.drawable.red_round, R.drawable.brown_round, R.drawable.blue_round,
                R.drawable.blue_grey_round, R.drawable.yellow_round, R.drawable.deep_purple_round,
                R.drawable.pink_round, R.drawable.green_round, R.drawable.deep_orange_round,
                R.drawable.grey_round, R.drawable.cyan_round,R.drawable.amber_round};
        List<Integer> list = Arrays.asList(res);
        //初始化颜色adapter
        mColorAdapter = new ColorListAdapter(getActivity(), list);
        //设置当前的选择项
        mColorAdapter.setCheckItem(ThemeUtils.getCurrentTheme(getActivity()).getIntValue());

        mGridView = (GridView) LayoutInflater.from(getActivity()).inflate(R.layout.colors_panel_layout, null);
        mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        mGridView.setCacheColorHint(0);
        mGridView.setAdapter(mColorAdapter);
        builder.setView(mGridView);

        final AlertDialog dialog = builder.show();
        mGridView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        dialog.dismiss();
                        int value = ThemeUtils.getCurrentTheme(getActivity()).getIntValue();
                        if (value != position) {
                            preferenceUtils.saveParam(getString(R.string.change_theme_key), position);
                            TypedValue typedValue = new TypedValue();
                            Resources.Theme theme = getContext().getTheme();
                            theme.resolveAttribute(R.attr.mainBackground, typedValue, true);
                            changeTheme(ThemeUtils.Theme.mapValueToTheme(position));
                        }
                    }
                }
        );
    }
    private void changeTheme(ThemeUtils.Theme theme) {

        if (getActivity() == null)
            return;
        // getActivity().finish();
         EventBus.getDefault().post(theme);

    }
}
