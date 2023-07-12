package com.dynamsoft.ddn.helloworld.ui;

import static com.dynamsoft.ddn.helloworld.utils.CvrUtil.normalizeImageDataByQuad;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.dce.QuadDrawingItem;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentEditBinding;

public class EditFragment extends Fragment {
    private FragmentEditBinding binding;
    private MainViewModel viewModel;
    private CaptureVisionRouter cvr;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnNormalize.setOnClickListener(v -> {
            QuadDrawingItem selectedItem = (QuadDrawingItem) binding.imageEditorView.getSelectedDrawingItem();
            if (selectedItem == null) {
                Toast.makeText(requireActivity(), "Please selected an item.", Toast.LENGTH_SHORT).show();
                return;
            }
            viewModel.colourNormalizedImageData = normalizeImageDataByQuad(viewModel.cvr, viewModel.capturedImageData, selectedItem.getQuad());
            requireActivity().runOnUiThread(() ->
                    NavHostFragment.findNavController(EditFragment.this)
                            .navigate(R.id.action_editFragment_to_normalizeFragment));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}