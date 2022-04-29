package com.dynamsoft.ddn.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dynamsoft.dce.DCEDrawingLayer;
import com.dynamsoft.dce.DCEImageEditorView;
import com.dynamsoft.dce.DrawingItem;
import com.dynamsoft.dce.EnumDrawingItemState;
import com.dynamsoft.dce.QuadDrawingItem;
import com.dynamsoft.ddn.DetectedQuadResult;
import com.dynamsoft.ddn.DocumentNormalizerException;
import com.dynamsoft.ddn.NormalizedImageResult;

import java.util.ArrayList;

import static com.dynamsoft.ddn.helloworld.MainActivity.mImageData;
import static com.dynamsoft.ddn.helloworld.MainActivity.mNormalizer;
import static com.dynamsoft.ddn.helloworld.MainActivity.mQuadResults;

public class QuadEditActivity extends AppCompatActivity {
    private static final String TAG = "QuadEditActivity";
    DCEImageEditorView imageEditView;
    public static NormalizedImageResult mImageResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quad_edit);
        imageEditView = findViewById(R.id.editor_view);

        imageEditView.setOriginalImage(mImageData);
        DCEDrawingLayer layer = imageEditView.getDrawingLayer(DCEDrawingLayer.DDN_LAYER_ID);

        ArrayList<DrawingItem> items = new ArrayList<DrawingItem>();
        for (DetectedQuadResult r : mQuadResults) {
            items.add(new QuadDrawingItem(r.location));
        }
        layer.setDrawingItems(items);
    }

    public void onNormalizeBtnClick(View v) {
        try {
            DrawingItem item = imageEditView.getSelectedDrawingItem();
            if(item == null)
                item = imageEditView.getDrawingLayer(DCEDrawingLayer.DDN_LAYER_ID).getDrawingItems().get(0);

            if(item instanceof QuadDrawingItem) {
                mImageResult = mNormalizer.normalize(mImageData, ((QuadDrawingItem) item).getQuad());

                Intent intent = new Intent(QuadEditActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        } catch (DocumentNormalizerException e) {
            e.printStackTrace();
        }
    }
}
