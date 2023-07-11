package com.dynamsoft.ddn.helloworld.util;

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
            SimplifiedCaptureVisionSettings oldSettings = cvr.getSimplifiedSettings(EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
            SimplifiedCaptureVisionSettings newSettings = cvr.getSimplifiedSettings(EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
            newSettings.roi = quadrilateral;
            newSettings.roiMeasuredInPercentage = false;
            cvr.updateSettings(newSettings, EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
            CapturedResult result = cvr.capture(imageData, EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
            cvr.updateSettings(oldSettings, EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
            if(result != null && result.getItems() != null && result.getItems().length > 0) {
                return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
            }
        } catch (CaptureException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public static ImageData normalizeInDifferentColorMode(CaptureVisionRouter cvr, ImageData imageData, @ColorMode int colorMode) {
        if(cvr == null || imageData == null) {
            return null;
        }
        String templateName = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT;
        try {
            if (colorMode == ColorMode.COLOR_MODE_BINARY) {
                templateName = "normalize-document-binary";
            } else if (colorMode == ColorMode.COLOR_MODE_GRAYSCALE) {
                templateName = "normalize-document-grayscale";
            }
            CapturedResult result = cvr.capture(imageData, templateName);
            if (result != null && result.getItems() != null && result.getItems().length > 0) {
                return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
            }
        } catch (CaptureException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
