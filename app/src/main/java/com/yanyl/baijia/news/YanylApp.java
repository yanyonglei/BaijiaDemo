package com.yanyl.baijia.news;


import android.os.Environment;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.io.File;



/**
 * Created by yanyl on 2016/10/23.
 */
public class YanylApp extends LitePalApplication {

    private static YanylApp app;
    private RequestQueue queue;

    public static YanylApp getApp() {
        return app;
    }
    public RequestQueue getQueue() {
        return queue;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化LitePal
        LitePal.initialize(this);
        this.app = this;
        //初始化 volley 框架
        initVolley();
        //初始化ImagerLoader
        initImageLoader();
    }


    private void initImageLoader() {
        //缓存 文件的路径 imgcache 文件名
        File imgcache = new File(Environment.getExternalStorageDirectory(), "imgcache");

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(3)
                .memoryCacheSize(10 << 20)
                .memoryCacheSizePercentage(12)
                .diskCacheSize(100 << 20)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .writeDebugLogs()
                .diskCache(new UnlimitedDiskCache(imgcache)).build();
        ImageLoader.getInstance().init(configuration);
    }
    private void initVolley() {
        queue = Volley.newRequestQueue(this);
    }
}
