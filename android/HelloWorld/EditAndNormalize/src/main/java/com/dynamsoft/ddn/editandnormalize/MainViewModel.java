package com.dynamsoft.ddn.editandnormalize;

import androidx.lifecycle.ViewModel;

import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.dce.DrawingItem;
import com.dynamsoft.dce.QuadDrawingItem;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {
    public ImageData normalizedImage;
    public ImageData capturedWholeImage;
    public Quadrilateral[] capturedQuads;

    public ArrayList<DrawingItem> getQuadDrawingItems() {
        if(capturedQuads == null) {
            return new ArrayList<>();
        }
        ArrayList<DrawingItem> items = new ArrayList<>();
        for (Quadrilateral quad : capturedQuads) {
            items.add(new QuadDrawingItem(quad));
        }
        return items;
    }
}
