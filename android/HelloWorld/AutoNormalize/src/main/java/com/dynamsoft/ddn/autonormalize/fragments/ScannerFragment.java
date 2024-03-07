package com.dynamsoft.ddn.autonormalize.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.dynamsoft.core.basic_structures.CompletionListener;
import com.dynamsoft.cvr.CapturedResultReceiver;
import com.dynamsoft.core.basic_structures.EnumCapturedResultItemType;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.EnumPresetTemplate;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.dce.CameraEnhancerException;
import com.dynamsoft.dce.EnumEnhancerFeatures;
import com.dynamsoft.ddn.NormalizedImagesResult;
import com.dynamsoft.ddn.autonormalize.MainViewModel;
import com.dynamsoft.ddn.autonormalize.R;
import com.dynamsoft.ddn.autonormalize.databinding.FragmentScannerBinding;
import com.dynamsoft.utility.MultiFrameResultCrossFilter;

import java.util.Locale;

public class ScannerFragment extends Fragment {

    private FragmentScannerBinding binding;
    private MainViewModel mViewModel;
    private CaptureVisionRouter mRouter;
    private CameraEnhancer mCamera;
    private AlertDialog mAlertDialog;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentScannerBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        initCaptureVisionRouter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCamera = new CameraEnhancer(binding.cameraView, getViewLifecycleOwner());
        try {
            // Enable the frame filter feature of Dynamsoft Camera Enhancer to process the high quality video frames only.
            mCamera.enableEnhancedFeatures(EnumEnhancerFeatures.EF_FRAME_FILTER);
            // You can use the following code to get normalized images with higher resolution.
            //mCamera.setResolution(EnumResolution.RESOLUTION_4K);
        } catch (CameraEnhancerException e) {
            e.printStackTrace();
        }
        try {
            // Set Dynamsoft Camera Enhancer as the input.
            mRouter.setInput(mCamera);
        } catch (CaptureVisionRouterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mCamera.open();
        } catch (CameraEnhancerException e) {
            e.printStackTrace();
        }
        mRouter.startCapturing(EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT,  new CompletionListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int errorCode, String errorString) {
                if(getActivity() != null){
                    getActivity().runOnUiThread(() -> showDialog("Error",
                            String.format(Locale.getDefault(),
                                    "ErrorCode: %d %nErrorMessage: %s", errorCode, errorString)));
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mCamera.close();
        } catch (CameraEnhancerException e) {
            e.printStackTrace();
        }
        mRouter.stopCapturing();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showDialog(String title, String message) {
        if (mAlertDialog == null) {
            // Restart the capture when the dialog is closed
            mAlertDialog = new AlertDialog.Builder(getContext()).setCancelable(true).setPositiveButton("OK", null)
                    .create();
        }
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(message);
        mAlertDialog.show();
    }

    private void initCaptureVisionRouter() {
        mRouter = new CaptureVisionRouter(requireActivity());
        // Enable the result cross verification so that the output of detected boundary will be more accurate.
        MultiFrameResultCrossFilter filter = new MultiFrameResultCrossFilter();
        filter.enableResultCrossVerification(EnumCapturedResultItemType.CRIT_NORMALIZED_IMAGE, true);
        // Add the result filter to capture vision router.
        mRouter.addResultFilter(filter);
        // Add result receiver to receive callback when the result output.
        mRouter.addResultReceiver(new CapturedResultReceiver() {
            @Override
            public void onNormalizedImagesReceived(NormalizedImagesResult result) {
                if(result.getItems() != null && result.getItems().length > 0) {
                    mViewModel.normalizedImage = result.getItems()[0].getImageData();
                    mRouter.stopCapturing();
                    requireActivity().runOnUiThread(()-> NavHostFragment.findNavController(ScannerFragment.this)
                            .navigate(R.id.action_ScannerFragment_to_NormalizeFragment));
                }
            }
        });
    }

}