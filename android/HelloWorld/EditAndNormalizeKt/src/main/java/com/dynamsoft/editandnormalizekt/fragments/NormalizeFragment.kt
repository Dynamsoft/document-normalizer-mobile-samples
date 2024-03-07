package com.dynamsoft.editandnormalizekt.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dynamsoft.core.basic_structures.CoreException
import com.dynamsoft.core.basic_structures.ImageData
import com.dynamsoft.cvr.CaptureVisionRouter
import com.dynamsoft.cvr.CaptureVisionRouterException
import com.dynamsoft.cvr.CapturedResult
import com.dynamsoft.cvr.EnumPresetTemplate
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings
import com.dynamsoft.ddn.EnumImageColourMode
import com.dynamsoft.ddn.NormalizedImageResultItem
import com.dynamsoft.editandnormalizekt.MainViewModel
import com.dynamsoft.editandnormalizekt.databinding.FragmentNormalizeBinding

class NormalizeFragment : Fragment() {
    private var _binding: FragmentNormalizeBinding? = null
    private val binding get() = _binding!!
    private var router: CaptureVisionRouter? = null
    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNormalizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        if (viewModel.normalizedImage != null) {
            try {
                binding.ivNormalize.setImageBitmap(viewModel.normalizedImage!!.toBitmap())
            } catch (e: CoreException) {
                e.printStackTrace()
            }
            binding.btnBinary.setOnClickListener { v ->
                showNormalizeInDifferentColorMode(
                    EnumImageColourMode.ICM_BINARY
                )
            }
            binding.btnGrayscale.setOnClickListener { v ->
                showNormalizeInDifferentColorMode(
                    EnumImageColourMode.ICM_GRAYSCALE
                )
            }
            binding.btnColour.setOnClickListener { v ->
                showNormalizeInDifferentColorMode(
                    EnumImageColourMode.ICM_COLOUR
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showNormalizeInDifferentColorMode(@EnumImageColourMode colorMode: Int) {
        val normalize: ImageData? = getNormalizeInDifferentColorMode(colorMode)
        if (normalize != null) {
            try {
                binding.ivNormalize.setImageBitmap(normalize.toBitmap())
            } catch (e: CoreException) {
                e.printStackTrace()
            }
        }
    }

    private fun getNormalizeInDifferentColorMode(@EnumImageColourMode colorMode: Int): ImageData? {
        if (router == null) {
            router = CaptureVisionRouter(requireContext())
        }
        try {
            val simplifiedSettings: SimplifiedCaptureVisionSettings =
                router!!.getSimplifiedSettings(EnumPresetTemplate.PT_NORMALIZE_DOCUMENT)
            simplifiedSettings.documentSettings?.colourMode = colorMode
            router!!.updateSettings(EnumPresetTemplate.PT_NORMALIZE_DOCUMENT, simplifiedSettings)
        } catch (e: CaptureVisionRouterException) {
            e.printStackTrace()
        }
        val result: CapturedResult =
            router!!.capture(viewModel.normalizedImage, EnumPresetTemplate.PT_NORMALIZE_DOCUMENT)
        return if (result.items.isNotEmpty()) {
            (result.items[0] as NormalizedImageResultItem).imageData
        } else null
    }
}