package com.dynamsoft.editandnormalizekt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.dynamsoft.core.basic_structures.CoreException
import com.dynamsoft.core.basic_structures.ImageData
import com.dynamsoft.core.basic_structures.Quadrilateral
import com.dynamsoft.cvr.CaptureVisionRouter
import com.dynamsoft.cvr.CaptureVisionRouterException
import com.dynamsoft.cvr.CapturedResult
import com.dynamsoft.cvr.EnumPresetTemplate
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings
import com.dynamsoft.dce.DrawingLayer
import com.dynamsoft.dce.QuadDrawingItem
import com.dynamsoft.ddn.NormalizedImageResultItem
import com.dynamsoft.editandnormalizekt.MainViewModel
import com.dynamsoft.editandnormalizekt.R
import com.dynamsoft.editandnormalizekt.databinding.FragmentEditorBinding

class EditorFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: MainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        val binding: FragmentEditorBinding =
            FragmentEditorBinding.inflate(inflater, container, false)
        binding.btnNormalize.setOnClickListener { v ->
            binding.imageEditorView.selectedDrawingItem?.let {
                val selectedItem: QuadDrawingItem = it as QuadDrawingItem
                viewModel.normalizedImage =
                    normalizeImageDataByQuad(viewModel.capturedWholeImage, selectedItem.quad)
                NavHostFragment.findNavController(this@EditorFragment)
                    .navigate(R.id.action_EditorFragment_to_NormalizeFragment)
            }
        }
        try {
            binding.imageEditorView.setOriginalImage(viewModel.capturedWholeImage?.toBitmap())
        } catch (e: CoreException) {
            e.printStackTrace()
        }
        binding.imageEditorView.getDrawingLayer(DrawingLayer.DDN_LAYER_ID).drawingItems = viewModel.quadDrawingItems
        return binding.root
    }

    private fun normalizeImageDataByQuad(
        imageData: ImageData?,
        quadrilateral: Quadrilateral?
    ): ImageData? {
        if (imageData == null || quadrilateral == null) {
            return null
        }
        val router = CaptureVisionRouter(requireContext())
        val template: String = EnumPresetTemplate.PT_NORMALIZE_DOCUMENT
        try {
            val settings: SimplifiedCaptureVisionSettings = router.getSimplifiedSettings(template)
            // Set the detected boundary received in the previous step as the new ROI.
            settings.roi = quadrilateral
            settings.roiMeasuredInPercentage = false
            router.updateSettings(template, settings)
        } catch (e: CaptureVisionRouterException) {
            e.printStackTrace()
        }

        // Use the capture method to process the image.
        val result: CapturedResult = router.capture(imageData, template)
        return if (result.items.isNotEmpty()) {
            (result.items[0] as NormalizedImageResultItem).imageData
        } else {
            null
        }
    }
}