package com.dynamsoft.ddn.helloworld;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.dyanmsoft.license.LicenseManager;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private String LICENSE =
            "DLS2eyJoYW5kc2hha2VDb2RlIjoiMTAwMjU2NDAxLTEwMDgwMDUzOSIsIm1haW5TZXJ2ZXJVUkwiOiJodHRwczovL210cGwuZHluYW1zb2Z0LmNvbS8iLCJvcmdhbml6YXRpb25JRCI6IjEwMDI1NjQwMSIsInN0YW5kYnlTZXJ2ZXJVUkwiOiJodHRwczovL210cGxyZXMuZHluYW1zb2Z0LmNvbS8iLCJjaGVja0NvZGUiOjEwOTU5NDgyMzR9";

    @Override
    public void onCreate() {
        super.onCreate();
        LicenseManager.initLicense(LICENSE, this, (isSuccess, error) -> {
            if(!isSuccess) {
                Log.e(TAG, "InitLicense Error: "+error);
            }
        });
    }

}
