package com.dynamsoft.ddn.helloworld.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentNormalizeBinding;
import com.dynamsoft.ddn.helloworld.modes.ScanMode;
import com.dynamsoft.ddn.helloworld.utils.CvrUtil;

public class NormalizeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        //In Scan & Edit mode, colourNormalizedImageData will be assigned in EditFragment.
        if (viewModel.scanMode == ScanMode.AUTO_SCAN_MODE) {
            viewModel.colourNormalizedImageData = CvrUtil.normalizeImageDataByQuad(viewModel.cvr, viewModel.capturedWholeImage, viewModel.filteredQuad);
        }
        viewModel.normalizedImageData.postValue(viewModel.colourNormalizedImageData);

        FragmentNormalizeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normalize, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

}