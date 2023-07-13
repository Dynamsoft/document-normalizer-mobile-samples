package com.dynamsoft.ddn.helloworld.ui;

import android.widget.ImageView;

import com.dynamsoft.core.basic_structures.CaptureException;
import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.dce.DrawingItem;
import com.dynamsoft.dce.DrawingLayer;
import com.dynamsoft.dce.ImageEditorView;
import com.dynamsoft.dce.QuadDrawingItem;

import java.util.ArrayList;

public class BindingAdapter {
    @androidx.databinding.BindingAdapter("EditorViewImage")
    public static void loadImageDataOnEditorView(ImageEditorView view, ImageData imageData) {
        if(imageData != null) {
            view.setOriginalImage(imageData);
        }
    }

    @androidx.databinding.BindingAdapter("EditorViewQuads")
    public static void loadQuadsOnEditorViewDDNLayer(ImageEditorView view, Quadrilateral[] quads) {
        ArrayList<DrawingItem> drawingItems = new ArrayList<>();
        for (Quadrilateral quad : quads) {
            drawingItems.add(new QuadDrawingItem(quad));
        }
        view.getDrawingLayer(DrawingLayer.DDN_LAYER_ID).setDrawingItems(drawingItems);
    }
    
    @androidx.databinding.BindingAdapter("ImageData")
    public static void loadImageDataOnImageView(ImageView view, ImageData imageData) {
        if(imageData != null) {
            try {
                view.setImageBitmap(imageData.toBitmap());
            } catch (CaptureException e) {
                e.printStackTrace();
            }
        }
    }

}
