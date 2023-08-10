package com.dynamsoft.ddn.helloworld.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentStartBinding;

public class StartFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        FragmentStartBinding binding = FragmentStartBinding.inflate(inflater, container, false);
        binding.btnScanEdit.setOnClickListener(v -> {
            viewModel.template = EnumPresetTemplate.PT_DETECT_DOCUMENT_BOUNDARIES;
            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.action_StartFragment_to_ScannerFragment);
        });
        binding.btnAutoScan.setOnClickListener(v -> {
            viewModel.template = EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT;
            NavHostFragment.findNavController(StartFragment.this)
                    .navigate(R.id.action_StartFragment_to_ScannerFragment);
        });
        return binding.getRoot();
    }

}