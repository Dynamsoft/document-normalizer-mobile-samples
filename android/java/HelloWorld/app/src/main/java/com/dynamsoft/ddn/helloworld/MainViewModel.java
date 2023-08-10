package com.dynamsoft.ddn.helloworld;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.ddn.helloworld.modes.ColorMode;
import com.dynamsoft.ddn.helloworld.utils.CvrUtil;

public class MainViewModel extends ViewModel {

    /*
     EnumPresetTemplate.PT_DETECT_DOCUMENT_BOUNDARIES
      or
     EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT
     */
    public String template = EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT;
    public ImageData capturedWholeImage;

    //Used in scan edit mode
    public Quadrilateral[] capturedQuads;

    public ImageData colourNormalizedImageData;

    //Used in NormalizeFragment
    public MutableLiveData<ImageData> normalizedImageData = new MutableLiveData<>();

    /**
     * This method is called when the user clicks on the "Color Mode" buttons in the NormalizeFragment.
     *
     * @param colorMode the color mode to use for normalization
     * @see com.dynamsoft.ddn.helloworld.ui.NormalizeFragment
     */
    public void setNormalizedImageDataByColorImageData(CaptureVisionRouter cvr, @ColorMode int colorMode) {
        if (cvr == null || colourNormalizedImageData == null) {
            return;
        }
        ImageData imageData = CvrUtil.normalizeInDifferentColorMode(cvr, colourNormalizedImageData, colorMode);
        this.normalizedImageData.postValue(imageData);
    }
}
