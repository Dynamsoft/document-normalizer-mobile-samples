package com.dynamsoft.ddn.autonormalize.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dynamsoft.cvr.CapturedResult;
import com.dynamsoft.core.basic_structures.CoreException;
import com.dynamsoft.core.basic_structures.ImageData;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings;
import com.dynamsoft.ddn.EnumImageColourMode;
import com.dynamsoft.ddn.NormalizedImageResultItem;
import com.dynamsoft.ddn.autonormalize.MainViewModel;
import com.dynamsoft.ddn.autonormalize.databinding.FragmentNormalizeBinding;

public class NormalizeFragment extends Fragment {

    private FragmentNormalizeBinding binding;
    private CaptureVisionRouter router;
    private MainViewModel viewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNormalizeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        try {
            assert viewModel.normalizedImage != null;
            binding.ivNormalize.setImageBitmap(viewModel.normalizedImage.toBitmap());
        } catch (CoreException e) {
            e.printStackTrace();
        }
        binding.btnBinary.setOnClickListener(v->showNormalizeInDifferentColorMode(EnumImageColourMode.ICM_BINARY));
        binding.btnGrayscale.setOnClickListener(v->showNormalizeInDifferentColorMode(EnumImageColourMode.ICM_GRAYSCALE));
        binding.btnColour.setOnClickListener(v->showNormalizeInDifferentColorMode(EnumImageColourMode.ICM_COLOUR));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showNormalizeInDifferentColorMode(@EnumImageColourMode int colorMode) {
        ImageData normalize = getNormalizeInDifferentColorMode(colorMode);
        if(normalize != null) {
            try {
                binding.ivNormalize.setImageBitmap(normalize.toBitmap());
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
    }

    private ImageData getNormalizeInDifferentColorMode(@EnumImageColourMode int colorMode) {
        if(router == null) {
            router = new CaptureVisionRouter(requireContext());
        }
        try {
            SimplifiedCaptureVisionSettings simplifiedSettings = router.getSimplifiedSettings(EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
            simplifiedSettings.documentSettings.colourMode = colorMode;
            router.updateSettings(EnumPresetTemplate.PT_NORMALIZE_DOCUMENT, simplifiedSettings);
        } catch (CaptureVisionRouterException e) {
            e.printStackTrace();
        }
        CapturedResult result = router.capture(viewModel.normalizedImage, EnumPresetTemplate.PT_NORMALIZE_DOCUMENT);
        if (result != null && result.getItems() != null && result.getItems().length > 0) {
            return ((NormalizedImageResultItem) result.getItems()[0]).getImageData();
        }
        return null;
    }

}