package com.dynamsoft.ddn.helloworld;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dynamsoft.core.CoreException;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ImageView ivNormalize = findViewById(R.id.iv_normalize);

        try {
            ivNormalize.setImageBitmap(QuadEditActivity.mImageResult.image.toBitmap());
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }
}
