package com.dynamsoft.ddn.helloworld.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentNormalizeBinding;

public class NormalizeFragment extends Fragment {
    private CaptureVisionRouter cvr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cvr = new CaptureVisionRouter(requireContext());
        try {
            cvr.initSettingsFromFile("ddn-mobile-sample.json");
        } catch (CaptureVisionRouterException e) {
            e.printStackTrace();
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        viewModel.normalizedImageData.postValue(viewModel.colourNormalizedImageData);
        FragmentNormalizeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_normalize, container, false);
        binding.setViewModel(viewModel);
        binding.setCvr(cvr);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

}