package com.nianlun.libbase;

import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;

public class BaseApplication extends Application {

    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRouter(this);
    }

    public static Context getInstance() {
        return instance;
    }

    private void initRouter(BaseApplication mApplication) {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(mApplication);
    }
}
