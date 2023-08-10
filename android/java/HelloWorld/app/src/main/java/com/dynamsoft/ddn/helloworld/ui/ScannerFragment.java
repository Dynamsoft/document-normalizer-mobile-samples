package com.dynamsoft.ddn.helloworld.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.dynamsoft.ddn.DetectedQuadsResult;
import com.dynamsoft.ddn.NormalizedImagesResult;
import com.dynamsoft.ddn.helloworld.MainViewModel;
import com.dynamsoft.ddn.helloworld.R;
import com.dynamsoft.ddn.helloworld.databinding.FragmentScannerBinding;
import com.dynamsoft.utility.MultiFrameResultCrossFilter;

public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding;
    private MainViewModel viewModel;
    private CaptureVisionRouter cvr;
    private CameraEnhancer dce;

    private boolean ifNeedToShowResult;
    private boolean ifBtnClick;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.requestCameraPermission(requireActivity());
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        initCaptureVisionRouter();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scanner, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCapture.setOnClickListener(v -> ifBtnClick = true);
        dce = new CameraEnhancer(binding.cameraView, getViewLifecycleOwner());
        if (viewModel.template.equals(EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT)) {
            try {
                dce.enableEnhancedFeatures(EnumEnhancerFeatures.EF_FRAME_FILTER);
            } catch (CameraEnhancerException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            cvr.setInput(dce);
        } catch (CaptureVisionRouterException e) {
            throw new RuntimeException(e);
        }

    }

    private void initCaptureVisionRouter() {
        cvr = new CaptureVisionRouter(requireActivity());

        //Auto-scan Mode
        if (viewModel.template.equals(EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT)) {
            MultiFrameResultCrossFilter filter = new MultiFrameResultCrossFilter();
            filter.enableResultCrossVerification(EnumCapturedResultItemType.CRIT_NORMALIZED_IMAGE, true);
            cvr.addResultFilter(filter);
        }

        cvr.addResultReceiver(new CapturedResultReceiver() {
            @Override
            public void onDetectedQuadsReceived(DetectedQuadsResult result) {
                if (ifBtnClick && result.getItems().length > 0) {
                    ifBtnClick = false;
                    viewModel.capturedQuads = new Quadrilateral[result.getItems().length];
                    for (int i = 0; i < result.getItems().length; i++) {
                        viewModel.capturedQuads[i] = result.getItems()[i].getLocation();
                    }
                    viewModel.capturedWholeImage = cvr.getIntermediateResultManager().getOriginalImage(result.getOriginalImageHashId());
                    requireActivity().runOnUiThread(() -> {
                        if (!ScannerFragment.this.isDetached()) {
                            NavHostFragment.findNavController(ScannerFragment.this)
                                    .navigate(R.id.action_ScannerFragment_to_editFragment);
                        }
                    });
                }
            }

            @Override
            public void onNormalizedImagesReceived(NormalizedImagesResult result) {
                //Auto Scan Mode
                if (ifNeedToShowResult && result.getItems().length > 0) {
                    ifNeedToShowResult = false;
                    viewModel.colourNormalizedImageData = result.getItems()[0].getImageData();
                    requireActivity().runOnUiThread(() -> {
                        if (!ScannerFragment.this.isDetached()) {
                            NavHostFragment.findNavController(ScannerFragment.this)
                                    .navigate(R.id.action_ScannerFragment_to_normalizeFragment);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            dce.open();
            cvr.startCapturing(viewModel.template);
        } catch (CaptureVisionRouterException | CameraEnhancerException e) {
            e.printStackTrace();
        }
        ifNeedToShowResult = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        ifNeedToShowResult = false;
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