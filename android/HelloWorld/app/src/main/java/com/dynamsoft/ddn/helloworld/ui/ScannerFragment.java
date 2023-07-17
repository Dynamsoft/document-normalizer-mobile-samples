package com.dynamsoft.ddn.helloworld.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.dynamsoft.core.basic_structures.CapturedResultReceiver;
import com.dynamsoft.core.basic_structures.EnumCapturedResultItemType;
import com.dynamsoft.core.basic_structures.Quadrilateral;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.dce.CameraEnhancerException;
import com.dynamsoft.dce.EnumEnhancerFeatures;
import com.dynamsoft.dce.utils.PermissionUtil;
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            initCaptureVisionRouter();
        } catch (CaptureVisionRouterException e) {
            throw new RuntimeException(e);
        }

        dce = new CameraEnhancer(binding.cameraView, getViewLifecycleOwner());
        try {
            dce.enableEnhancedFeatures(EnumEnhancerFeatures.EF_FRAME_FILTER);
        } catch (CameraEnhancerException e) {
            throw new RuntimeException(e);
        }
        cvr.setInput(dce);
        binding.btnCapture.setOnClickListener(v -> ifNeedToQuadEdit = true);
    }

    public void initCaptureVisionRouter() throws CaptureVisionRouterException {
        viewModel.cvr = new CaptureVisionRouter(requireActivity());

        cvr = viewModel.cvr;
        cvr.initSettingsFromFile("ddn-mobile-sample.json");

        if (viewModel.scanMode == ScanMode.AUTO_SCAN_MODE) {
            MultiFrameResultCrossFilter filter = new MultiFrameResultCrossFilter();
            filter.enableResultVerification(EnumCapturedResultItemType.CRIT_DETECTED_QUAD, true);
            cvr.addResultFilter(filter);
        }

        cvr.addResultReceiver(new CapturedResultReceiver() {
            @Override
            public void onDetectedQuadsReceived(DetectedQuadsResult result) {
                if (getActivity() == null) {
                    return;
                }

                //Scan & Edit Mode
                if (viewModel.scanMode == ScanMode.SCAN_EDIT_MODE) {
                    if (ifNeedToQuadEdit && result.getItems().length > 0) {
                        ifNeedToQuadEdit = false;
                        viewModel.capturedQuads = new Quadrilateral[result.getItems().length];
                        for (int i = 0; i < result.getItems().length; i++) {
                            viewModel.capturedQuads[i] = result.getItems()[i].getLocation();
                        }
                        viewModel.capturedWholeImage = cvr.getIntermediateResultManager().getRawImage(result.getSourceImageHashId());
                        getActivity().runOnUiThread(() ->
                                NavHostFragment.findNavController(ScannerFragment.this)
                                        .navigate(R.id.action_ScannerFragment_to_editFragment));
                    }
                }

                //Auto Scan Mode
                if (viewModel.scanMode == ScanMode.AUTO_SCAN_MODE) {
                    if (!ifJumpToNextFg && result.getItems().length > 0) {
                        viewModel.capturedWholeImage = cvr.getIntermediateResultManager().getRawImage(result.getSourceImageHashId());
                        DetectedQuadResultItem selectedItem = result.getItems()[0];
                        for (DetectedQuadResultItem item : result.getItems()) {
                            if (item.getConfidenceAsDocumentBoundary() > selectedItem.getConfidenceAsDocumentBoundary()) {
                                selectedItem = item;
                            }
                        }
                        viewModel.filteredQuad = selectedItem.getLocation();

                        ifJumpToNextFg = true;
                        getActivity().runOnUiThread(() ->
                                NavHostFragment.findNavController(ScannerFragment.this)
                                        .navigate(R.id.action_ScannerFragment_to_normalizeFragment));
                    }
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        ifJumpToNextFg = false;
        try {
            dce.open();
            cvr.startCapturing(EnumPresetTemplate.PT_DETECT_DOCUMENT_BOUNDARIES);
        } catch (CaptureVisionRouterException | CameraEnhancerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            dce.close();
        } catch (CameraEnhancerException e) {
            e.printStackTrace();
        }
        cvr.stopCapturing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}