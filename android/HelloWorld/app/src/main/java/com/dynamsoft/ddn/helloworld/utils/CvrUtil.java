package com.dynamsoft.ddn.helloworld.utils;

import com.dynamsoft.core.basic_structures.CaptureException;
import com.dynamsoft.core.basic_structures.CapturedResult;
import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings;
import com.dynamsoft.ddn.NormalizedImageResultItem;
import com.dynamsoft.ddn.helloworld.modes.ColorMode;

public class CvrUtil {
    public static ImageData normalizeImageDataByQuad(CaptureVisionRouter cvr, ImageData imageData, Quadrilateral quadrilateral) {
        if(cvr == null || imageData == null || quadrilateral == null) {
            return null;
        }
        try {
            String template = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT;
            SimplifiedCaptureVisionSettings oldSettings = cvr.getSimplifiedSettings(template);
            SimplifiedCaptureVisionSettings newSettings = cvr.getSimplifiedSettings(template);

            newSettings.roi = quadrilateral;
            newSettings.roiMeasuredInPercentage = false;
            cvr.updateSettings(newSettings, template);
            CapturedResult result = cvr.capture(imageData, template);

            //reset settings of normalize-document template
            cvr.updateSettings(oldSettings, template);
            if(result != null && result.getItems() != null && result.getItems().length > 0) {
                return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
            }
        } catch (CaptureException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ImageData normalizeInDifferentColorMode(CaptureVisionRouter cvr, ImageData imageData, @ColorMode int colorMode) {
        if(cvr == null || imageData == null) {
            return null;
        }
        try {
            String templateName;
            if (colorMode == ColorMode.COLOR_MODE_BINARY) {
                templateName = "normalize-document-binary";
            } else if (colorMode == ColorMode.COLOR_MODE_GRAYSCALE) {
                templateName = "normalize-document-grayscale";
            } else { //Colour
                templateName = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT;
            }
            CapturedResult result = cvr.capture(imageData, templateName);
            if (result != null && result.getItems() != null && result.getItems().length > 0) {
                return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
            }
        } catch (CaptureException e) {
            e.printStackTrace();
        }
        return null;
    }


}
