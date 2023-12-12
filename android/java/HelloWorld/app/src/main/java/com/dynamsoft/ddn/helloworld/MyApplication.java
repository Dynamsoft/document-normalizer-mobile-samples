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
        // Initialize license for Dynamsoft Document Normalizer.
        // The license string here is a time-limited trial license. Note that network connection is required for this license to work.
        // You can also request an extension for your trial license in the customer portal: https://www.dynamsoft.com/customer/license/trialLicense?product=ddn&utm_source=installer&package=android 
        LicenseManager.initLicense(LICENSE, this, (isSuccess, error) -> {
            if (!isSuccess) {
                Log.e(TAG, "InitLicense Error: " + error);
            }
        });
    }

}
