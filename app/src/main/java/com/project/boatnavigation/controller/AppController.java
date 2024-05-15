package com.project.boatnavigation.controller;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;




public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();
 

    private static AppController mInstance;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        //initImageLoader(this);
        //initImageLoader(this);

    }


    public static Context getAppContext(){
		return mInstance;
    	
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;

    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }
 

//    public static void initImageLoader(Context context) {
//        // This configuration tuning is custom. You can tune every option, you
//        // may tune some of them,
//        // or you can create default configuration by
//        // ImageLoaderConfiguration.createDefault(this);
//        // method.
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
//                context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config.build());
//    }

 
//    public <T> void addToRequestQueue(Request<T> req, String tag) {
//        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
//        getRequestQueue().add(req);
//    }
//
//    public <T> void addToRequestQueue(Request<T> req) {
//        req.setTag(TAG);
//        getRequestQueue().add(req);
//    }
//
//    public void cancelPendingRequests(Object tag) {
//        if (mRequestQueue != null) {
//            mRequestQueue.cancelAll(tag);
//        }
//    }
//
//    public static synchronized ObjectMapper getObjectMapper() {
//        if(objectMapper == null) {
//            objectMapper = new ObjectMapper();
//            objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
//        }
//        return objectMapper;
//    }
}
