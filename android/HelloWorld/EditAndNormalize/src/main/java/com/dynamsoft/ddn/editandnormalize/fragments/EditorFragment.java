package com.dynamsoft.ddn.editandnormalize.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.dynamsoft.core.basic_structures.CoreException;
import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.CapturedResult;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings;
import com.dynamsoft.dce.DrawingLayer;
import com.dynamsoft.dce.QuadDrawingItem;
import com.dynamsoft.ddn.NormalizedImageResultItem;
import com.dynamsoft.ddn.editandnormalize.MainViewModel;
import com.dynamsoft.ddn.editandnormalize.R;
import com.dynamsoft.ddn.editandnormalize.databinding.FragmentEditorBinding;

public class EditorFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        FragmentEditorBinding binding = FragmentEditorBinding.inflate(inflater, container, false);
        binding.btnNormalize.setOnClickListener(v -> {
            QuadDrawingItem selectedItem = (QuadDrawingItem) binding.imageEditorView.getSelectedDrawingItem();
            if (selectedItem == null) {
                Toast.makeText(requireActivity(), "Please select an item.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.normalizedImage = normalizeImageDataByQuad(viewModel.capturedWholeImage, selectedItem.getQuad());
                NavHostFragment.findNavController(EditorFragment.this).navigate(R.id.action_EditorFragment_to_NormalizeFragment);
            }
        });
        try {
            binding.imageEditorView.setOriginalImage(viewModel.capturedWholeImage.toBitmap());
        } catch (CoreException e) {
            e.printStackTrace();
        }
        binding.imageEditorView.getDrawingLayer(DrawingLayer.DDN_LAYER_ID)
                .setDrawingItems(viewModel.getQuadDrawingItems());
        return binding.getRoot();
    }

    private ImageData normalizeImageDataByQuad(ImageData imageData, Quadrilateral quadrilateral) {
        if (imageData == null || quadrilateral == null) {
            return null;
        }
        CaptureVisionRouter router = new CaptureVisionRouter(requireContext());
        String template = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT;
        try {
            SimplifiedCaptureVisionSettings settings = router.getSimplifiedSettings(template);
            // Set the detected boundary received in the previous step as the new ROI.
            settings.roi = quadrilateral;
            settings.roiMeasuredInPercentage = false;
            router.updateSettings(template, settings);
        } catch (CaptureVisionRouterException e) {
            e.printStackTrace();
        }

        // Use the capture method to process the image.
        CapturedResult result = router.capture(imageData, template);

        if (result.getItems() != null && result.getItems().length > 0) {
            return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
        } else {
            return null;
        }
    }
}