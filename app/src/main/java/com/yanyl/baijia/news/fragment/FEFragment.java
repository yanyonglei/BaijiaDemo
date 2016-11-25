package com.yanyl.baijia.news.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.yanyl.baijia.news.R;
import com.yanyl.baijia.news.atys.JieShaoActivity;
import com.yanyl.baijia.news.atys.LookPhotoActivity;
import com.yanyl.baijia.news.util.Util;

public class FEFragment extends BaseFragment {

    private TextView mPhotoView;
    private TextView mYuleTextView;


    public FEFragment(){}
    private static FEFragment feFragment=null;

    public static FEFragment getInstance(){
        if (feFragment==null){
			synchronized(FEFragment.class){
				if (feFragment==null)
				{
					feFragment=new FEFragment();
				}
			}
        }
		return feFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.te_fragment,container,false);
        mPhotoView= (TextView) view.findViewById(R.id.id_te_keji);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Util.showToast(getContext(),"mTechTextView");
                Util.enterIntent(getContext(), LookPhotoActivity.class);
            }
        });
        mYuleTextView= (TextView) view.findViewById(R.id.id_te_yule);

        mYuleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Util.enterIntent(getContext(), JieShaoActivity.class);

            }
        });
        return view;
    }
}
