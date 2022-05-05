package com.dynamsoft.ddn.helloworld;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dynamsoft.dce.DCEDrawingLayer;
import com.dynamsoft.dce.DCEImageEditorView;
import com.dynamsoft.dce.DrawingItem;
import com.dynamsoft.dce.QuadDrawingItem;
import com.dynamsoft.ddn.DetectedQuadResult;
import com.dynamsoft.ddn.DocumentNormalizerException;
import com.dynamsoft.ddn.NormalizedImageResult;

import java.util.ArrayList;

public class QuadEditActivity extends AppCompatActivity {
    private static final String TAG = "QuadEditActivity";
    private DCEImageEditorView mImageEditView;
    public static NormalizedImageResult mNormalizedImageResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quad_edit);

        // Add image editor view for interactive editing of quads
        mImageEditView = findViewById(R.id.editor_view);

        // Set the background image
        mImageEditView.setOriginalImage(MainActivity.mImageData);

        // Add detected quads as drawing items to the DDN drawing layer.
        DCEDrawingLayer layer = mImageEditView.getDrawingLayer(DCEDrawingLayer.DDN_LAYER_ID);

        ArrayList<DrawingItem> items = new ArrayList<DrawingItem>();
        for (DetectedQuadResult r : MainActivity.mQuadResults) {
            items.add(new QuadDrawingItem(r.location));
        }
        layer.setDrawingItems(items);
    }

    public void onNormalizeBtnClick(View v) {
        try {
            // Get the selected drawing item of DCEImageEditorView.
            DrawingItem item = mImageEditView.getSelectedDrawingItem();
            if(item == null)
                item = mImageEditView.getDrawingLayer(DCEDrawingLayer.DDN_LAYER_ID).getDrawingItems().get(0);

            if(item instanceof QuadDrawingItem) {
                // Normalize the image with the selected quad.
                mNormalizedImageResult = MainActivity.mNormalizer.normalize(MainActivity.mImageData, ((QuadDrawingItem) item).getQuad());

                // Start ResultActivity to display the final result.
                Intent intent = new Intent(QuadEditActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        } catch (DocumentNormalizerException e) {
            e.printStackTrace();
        }
    }
}
