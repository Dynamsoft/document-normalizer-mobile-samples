package com.dynamsoft.ddn.helloworld.utils;

import com.dynamsoft.core.basic_structures.CapturedResult;
import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings;
import com.dynamsoft.ddn.NormalizedImageResultItem;
import com.dynamsoft.ddn.helloworld.modes.ColorMode;

public class CvrUtil {
    public static ImageData normalizeImageDataByQuad(CaptureVisionRouter cvr, ImageData imageData, Quadrilateral quadrilateral) {
        if (cvr == null || imageData == null || quadrilateral == null) {
            return null;
        }
        try {
            String template = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT;
            SimplifiedCaptureVisionSettings oldSettings = cvr.getSimplifiedSettings(template);
            SimplifiedCaptureVisionSettings newSettings = cvr.getSimplifiedSettings(template);
            // Set the detected boundary received in the previous step as the new ROI.
            newSettings.roi = quadrilateral;
            newSettings.roiMeasuredInPercentage = false;
            cvr.updateSettings(template, newSettings);
            // Use the capture method to process the image.
            CapturedResult result = cvr.capture(imageData, template);

            // Reset settings of normalize-document template.
            cvr.updateSettings(template, oldSettings);
            if (result != null && result.getItems() != null && result.getItems().length > 0) {
                return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
            }
        } catch (CaptureVisionRouterException e) {
            e.printStackTrace();
        }
        return null;
    }

    // This method changes the colour mode of the normalized image.
    public static ImageData normalizeInDifferentColorMode(CaptureVisionRouter cvr, ImageData imageData, @ColorMode int colorMode) {
        if (cvr == null || imageData == null) {
            return null;
        }
        String templateName;
        if (colorMode == ColorMode.COLOR_MODE_BINARY) {
            // Switch to template "normalize-document-binary" to output binary image.
            templateName = "normalize-document-binary";
        } else if (colorMode == ColorMode.COLOR_MODE_GRAYSCALE) {
            // Switch to template "normalize-document-grayscale" to output grayscale image.
            templateName = "normalize-document-grayscale";
        } else { 
            // Switch to the default template to output colour image.
            templateName = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT;
        }
        CapturedResult result = cvr.capture(imageData, templateName);
        if (result != null && result.getItems() != null && result.getItems().length > 0) {
            return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
        }
        return null;
    }


}
