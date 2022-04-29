package com.dynamsoft.ddn.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dynamsoft.core.ImageData;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.dce.CameraEnhancerException;
import com.dynamsoft.dce.DCECameraView;
import com.dynamsoft.dce.DCEDrawingLayer;
import com.dynamsoft.ddn.DetectResultListener;
import com.dynamsoft.ddn.DetectedQuadResult;
import com.dynamsoft.ddn.DocumentNormalizer;
import com.dynamsoft.ddn.DocumentNormalizerException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static DocumentNormalizer mNormalizer;
    public static ImageData mImageData;
    public static DetectedQuadResult[] mQuadResults;

    private DCECameraView mCameraView;
    private CameraEnhancer mCamera;
    private boolean ifNeedToNormalize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mCameraView = findViewById(R.id.camera_view);

        mCamera = new CameraEnhancer(this);
        mCamera.setCameraView(mCameraView);

        mCameraView.getDrawingLayer(DCEDrawingLayer.DDN_LAYER_ID).setVisible(true);

        try {
            if (mNormalizer == null) {
                mNormalizer = new DocumentNormalizer();
            }
        } catch (DocumentNormalizerException e) {
            e.printStackTrace();
        }

        mNormalizer.setCameraEnhancer(mCamera);
        mNormalizer.setDetectResultListener(new DetectResultListener() {
            @Override
            public void detectResultCallback(int id, ImageData imageData, DetectedQuadResult[] results) {
                if (results != null && results.length > 0 && ifNeedToNormalize) {
                    ifNeedToNormalize = false;

                    mImageData = imageData;
                    mQuadResults = results;

                    Intent intent = new Intent(MainActivity.this, QuadEditActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mCamera.open();
        } catch (CameraEnhancerException e) {
            e.printStackTrace();
        }
        mNormalizer.startDetecting();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mCamera.close();
        } catch (CameraEnhancerException e) {
            e.printStackTrace();
        }
        mNormalizer.stopDetecting();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void onCaptureBtnClick(View v) {
        ifNeedToNormalize = true;
    }
}