package com.dynamsoft.ddn.helloworld;

import android.app.Application;
import android.util.Log;

import com.dynamsoft.license.LicenseManager;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static final String LICENSE =
            "DLS2eyJvcmdhbml6YXRpb25JRCI6IjIwMDAwMSJ9";

    @Override
    public void onCreate() {
        super.onCreate();
        LicenseManager.initLicense(LICENSE, this, (isSuccess, error) -> {
            if (!isSuccess) {
                Log.e(TAG, "InitLicense Error: " + error);
            }
        });
    }

}
