package com.dynamsoft.editandnormalizekt

import androidx.lifecycle.ViewModel
import com.dynamsoft.core.basic_structures.ImageData
import com.dynamsoft.core.basic_structures.Quadrilateral
import com.dynamsoft.dce.DrawingItem
import com.dynamsoft.dce.QuadDrawingItem

class MainViewModel : ViewModel() {
    var normalizedImage: ImageData? = null
    var capturedWholeImage: ImageData? = null
    var capturedQuads: Array<Quadrilateral?>? = null;
    val quadDrawingItems: ArrayList<DrawingItem<*>>
        get() {
            if (capturedQuads == null) {
                return ArrayList()
            }
            val items = ArrayList<DrawingItem<*>>()
            for (quad in capturedQuads!!) {
                items.add(QuadDrawingItem(quad!!))
            }
            return items
        }
}
