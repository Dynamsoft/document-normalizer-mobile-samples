package com.dynamsoft.ddn.helloworld.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.dynamsoft.core.basic_structures.CaptureException;
import com.dynamsoft.core.basic_structures.CapturedResultReceiver;
import com.dynamsoft.core.basic_structures.EnumCapturedResultItemType;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.dce.CameraEnhancerException;
import com.dynamsoft.dce.EnumEnhancerFeatures;
import com.dynamsoft.dce.util.PermissionUtil;
import com.dynamsoft.ddn.DetectedQuadResultItem;
import com.dynamsoft.ddn.DetectedQuadsResult;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentScannerBinding;
import com.dynamsoft.ddn.helloworld.modes.ScanMode;
import com.dynamsoft.utility.MultiFrameResultCrossFilter;

public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding;
    private MainViewModel viewModel;
    private CaptureVisionRouter cvr;
    private CameraEnhancer dce;

    private boolean ifNeedToQuadEdit = false;
    private boolean ifJumpToNextFg = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        PermissionUtil.requestCameraPermission(requireActivity());
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        Log.e("TAG", "onCreateView: "+viewModel.scanMode);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if(viewModel.cvr == null) {
            cvr = new CaptureVisionRouter(requireActivity());
            viewModel.cvr = cvr;

            try {
                cvr.initSettingsFromFile("ddn-mobile-sample.json");
            } catch (CaptureException e) {
                e.printStackTrace();
            }

            if(viewModel.scanMode == ScanMode.AUTO_SCAN_MODE) {
                MultiFrameResultCrossFilter filter = new MultiFrameResultCrossFilter();
                filter.enableResultVerification(EnumCapturedResultItemType.CRIT_DETECTED_QUAD, true);
                try {
                    cvr.addResultFilter(filter);
                } catch (CaptureException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        dce = new CameraEnhancer(binding.cameraView, getViewLifecycleOwner());
        try {
            dce.enableFeatures(EnumEnhancerFeatures.EF_FRAME_FILTER);
        } catch (CameraEnhancerException e) {
            throw new RuntimeException(e);
        }


        cvr.setInput(dce);

        cvr.addResultReceiver(new CapturedResultReceiver() {
            @Override
            public void onDetectedQuadsReceived(DetectedQuadsResult result) {

                //Scan & Edit Mode
                if(viewModel.scanMode == ScanMode.SCAN_EDIT_MODE ) {
                    if (ifNeedToQuadEdit && result != null && result.getItems() != null && result.getItems().length > 0) {
                        ifNeedToQuadEdit = false;
                        viewModel.capturedQuads = new Quadrilateral[result.getItems().length];
                        for (int i = 0; i < result.getItems().length; i++) {
                            viewModel.capturedQuads[i] = result.getItems()[i].getLocation();
                        }
                        viewModel.capturedImageData = cvr.getIntermediateResultManager().getRawImage(result.getSourceImageHashId());
                        requireActivity().runOnUiThread(() ->
                                NavHostFragment.findNavController(ScannerFragment.this)
                                        .navigate(R.id.action_ScannerFragment_to_editFragment));
                    }
                }

                //Auto Scan Mode
                if(viewModel.scanMode == ScanMode.AUTO_SCAN_MODE ) {
                    if(!ifJumpToNextFg && result != null && result.getItems() != null && result.getItems().length > 0) {
                        viewModel.capturedImageData = cvr.getIntermediateResultManager().getRawImage(result.getSourceImageHashId());
                        DetectedQuadResultItem selectedItem = result.getItems()[0];
                        for (DetectedQuadResultItem item : result.getItems()) {
                            if(item.getConfidenceAsDocumentBoundary() > selectedItem.getConfidenceAsDocumentBoundary()) {
                                selectedItem = item;
                            }
                        }
                        viewModel.filteredQuad = selectedItem.getLocation();

                        ifJumpToNextFg = true;
                        requireActivity().runOnUiThread(() ->
                                NavHostFragment.findNavController(ScannerFragment.this)
                                        .navigate(R.id.action_ScannerFragment_to_normalizeFragment));
                    }
                }
            }
        });

        binding.btnCapture.setOnClickListener(v -> {
            ifNeedToQuadEdit = true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ifJumpToNextFg = false;
        try {
            dce.open();
            cvr.startCapturing(EnumPresetTemplate.PT_DETECT_DOCUMENT_BOUNDARIES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            dce.close();
            cvr.stopCapturing();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}