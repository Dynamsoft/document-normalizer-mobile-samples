package com.dynamsoft.ddn.helloworld;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.ddn.helloworld.modes.ScanMode;

public class MainViewModel extends ViewModel {

    @ScanMode
    public int scanMode = ScanMode.SCAN_EDIT_MODE;

    public CaptureVisionRouter cvr = null;
    public ImageData capturedImageData = null;

    //Used in scan edit mode
    public Quadrilateral[] capturedQuads = null;

    //Used in auto scan mode
    public Quadrilateral filteredQuad = null;

    public ImageData colourNormalizedImageData = null;

    public MutableLiveData<ImageData> normalizedImageData = new MutableLiveData<>();
}
