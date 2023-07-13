package com.dynamsoft.ddn.helloworld;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.ddn.helloworld.modes.ColorMode;
import com.dynamsoft.ddn.helloworld.modes.ScanMode;
import com.dynamsoft.ddn.helloworld.utils.CvrUtil;

public class MainViewModel extends ViewModel {

    @ScanMode
    public int scanMode = ScanMode.SCAN_EDIT_MODE;

    public CaptureVisionRouter cvr;
    public ImageData capturedWholeImage;

    //Used in scan edit mode
    public Quadrilateral[] capturedQuads;

    //Used in auto scan mode
    public Quadrilateral filteredQuad;

    public ImageData colourNormalizedImageData;

    //Used in NormalizeFragment
    public MutableLiveData<ImageData> normalizedImageData = new MutableLiveData<>();

    /**
     * This method is called when the user clicks on the "Color Mode" buttons in the NormalizeFragment.
     *
     * @param colorMode the color mode to use for normalization
     * @see com.dynamsoft.ddn.helloworld.ui.NormalizeFragment
     */
    public void setNormalizedImageDataByColorImageData(@ColorMode int colorMode) {
        if (cvr == null || colourNormalizedImageData == null) {
            return;
        }
        ImageData imageData = CvrUtil.normalizeInDifferentColorMode(cvr, colourNormalizedImageData, colorMode);
        this.normalizedImageData.postValue(imageData);
    }
}
