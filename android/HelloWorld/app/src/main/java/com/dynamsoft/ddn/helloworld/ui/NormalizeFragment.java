package com.dynamsoft.ddn.helloworld.ui;

import static com.dynamsoft.ddn.helloworld.util.CvrUtil.normalizeImageDataByQuad;
import static com.dynamsoft.ddn.helloworld.util.CvrUtil.normalizeInDifferentColorMode;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentNormalizeBinding;
import com.dynamsoft.ddn.helloworld.modes.ColorMode;
import com.dynamsoft.ddn.helloworld.modes.ScanMode;

public class NormalizeFragment extends Fragment {
    private FragmentNormalizeBinding binding;
    private MainViewModel viewModel;
    private CaptureVisionRouter cvr;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if(viewModel.scanMode == ScanMode.AUTO_SCAN_MODE) {
            viewModel.colourNormalizedImageData = normalizeImageDataByQuad(viewModel.cvr, viewModel.capturedImageData, viewModel.filteredQuad);
        }
        viewModel.normalizedImageData.postValue(viewModel.colourNormalizedImageData);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normalize, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnBinary.setOnClickListener(v -> viewModel.normalizedImageData.postValue(normalizeInDifferentColorMode(viewModel.cvr, viewModel.colourNormalizedImageData, ColorMode.COLOR_MODE_BINARY)));
        binding.btnColour.setOnClickListener(v -> viewModel.normalizedImageData.postValue(normalizeInDifferentColorMode(viewModel.cvr, viewModel.colourNormalizedImageData, ColorMode.COLOR_MODE_COLOUR)));
        binding.btnGrayscale.setOnClickListener(v -> viewModel.normalizedImageData.postValue(normalizeInDifferentColorMode(viewModel.cvr, viewModel.colourNormalizedImageData, ColorMode.COLOR_MODE_GRAYSCALE)));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}