package com.dynamsoft.ddn.helloworld.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.dynamsoft.dce.QuadDrawingItem;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentEditBinding;
import com.dynamsoft.ddn.helloworld.utils.CvrUtil;

public class EditFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainViewModel viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        FragmentEditBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false);

        binding.btnNormalize.setOnClickListener(v -> {
            QuadDrawingItem selectedItem = (QuadDrawingItem) binding.imageEditorView.getSelectedDrawingItem();
            if (selectedItem == null) {
                Toast.makeText(requireActivity(), "Please selected an item.", Toast.LENGTH_SHORT).show();
            } else {
                viewModel.colourNormalizedImageData = CvrUtil.normalizeImageDataByQuad(viewModel.cvr, viewModel.capturedWholeImage, selectedItem.getQuad());
                NavHostFragment.findNavController(EditFragment.this).navigate(R.id.action_editFragment_to_normalizeFragment);
            }
        });

        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }
}