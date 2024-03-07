package com.dynamsoft.autonormalizekt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.dynamsoft.autonormalizekt.MainViewModel
import com.dynamsoft.autonormalizekt.R
import com.dynamsoft.autonormalizekt.databinding.FragmentScannerBinding
import com.dynamsoft.core.basic_structures.CompletionListener
import com.dynamsoft.core.basic_structures.EnumCapturedResultItemType
import com.dynamsoft.cvr.CaptureVisionRouter
import com.dynamsoft.cvr.CaptureVisionRouterException
import com.dynamsoft.cvr.CapturedResultReceiver
import com.dynamsoft.cvr.EnumPresetTemplate
import com.dynamsoft.dce.CameraEnhancer
import com.dynamsoft.dce.CameraEnhancerException
import com.dynamsoft.dce.EnumEnhancerFeatures
import com.dynamsoft.ddn.NormalizedImagesResult
import com.dynamsoft.utility.MultiFrameResultCrossFilter

class ScannerFragment : Fragment() {
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    private var viewModel: MainViewModel? = null
    private lateinit var router: CaptureVisionRouter
    private lateinit var camera: CameraEnhancer
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentScannerBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        initCaptureVisionRouter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camera = CameraEnhancer(binding.cameraView, viewLifecycleOwner)
        try {
            // Enable the frame filter feature of Dynamsoft Camera Enhancer to process the high quality video frames only.
            camera.enableEnhancedFeatures(EnumEnhancerFeatures.EF_FRAME_FILTER)
            // You can use the following code to get normalized images with higher resolution.
            //camera.setResolution(EnumResolution.RESOLUTION_4K);
        } catch (e: CameraEnhancerException) {
            e.printStackTrace()
        }
        try {
            // Set Dynamsoft Camera Enhancer as the input.
            router.input = camera
        } catch (e: CaptureVisionRouterException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            camera.open()
        } catch (e: CameraEnhancerException) {
            e.printStackTrace()
        }
        router.startCapturing(
            EnumPresetTemplate.PT_DETECT_AND_NORMALIZE_DOCUMENT,
            object : CompletionListener {
                override fun onSuccess() {}

                override fun onFailure(errorCode: Int, errorString: String?) {
                    activity?.runOnUiThread {
                        showDialog(
                            "Error",
                            "ErrorCode: $errorCode ErrorMessage: $errorString"
                        )
                    }
                }
            })
    }

    override fun onPause() {
        super.onPause()
        try {
            camera.close()
        } catch (e: CameraEnhancerException) {
            e.printStackTrace()
        }
        router.stopCapturing()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showDialog(title: String?, vararg messages: String) {
        android.app.AlertDialog.Builder(context).setTitle(title)
            .setItems(messages, null)
            .setCancelable(true)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun initCaptureVisionRouter() {
        router = CaptureVisionRouter(requireActivity())
        // Enable the result cross verification so that the output of detected boundary will be more accurate.
        val filter = MultiFrameResultCrossFilter()
        filter.enableResultCrossVerification(EnumCapturedResultItemType.CRIT_NORMALIZED_IMAGE, true)
        // Add the result filter to capture vision router.
        router.addResultFilter(filter)
        // Add result receiver to receive callback when the result output.
        router.addResultReceiver(object : CapturedResultReceiver {
            override fun onNormalizedImagesReceived(result: NormalizedImagesResult) {
                if (result.items.isNotEmpty()) {
                    viewModel!!.normalizedImage = result.items[0].imageData
                    router.stopCapturing()
                    requireActivity().runOnUiThread {
                        NavHostFragment.findNavController(this@ScannerFragment)
                            .navigate(R.id.action_ScannerFragment_to_NormalizeFragment)
                    }
                }
            }
        })
    }
}