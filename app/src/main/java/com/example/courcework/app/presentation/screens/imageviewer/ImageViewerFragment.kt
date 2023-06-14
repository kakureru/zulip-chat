package com.example.courcework.app.presentation.screens.imageviewer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.ImageLoader
import coil.load
import com.example.courcework.app.di.imageviewer.DaggerImageViewerComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.ViewModelFactory
import com.example.courcework.databinding.FragmentImageViewerBinding
import javax.inject.Inject

class ImageViewerFragment : Fragment() {

    private val uri: String by lazy { arguments?.getString(ARG_IMAGE_URI) ?: throw IllegalArgumentException() }

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val vm: ImageViewerViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentImageViewerBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerImageViewerComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentImageViewerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            image.load(uri, imageLoader)
            btnClose.setOnClickListener {
                vm.accept(IVEvent.BackPressed)
            }
        }
    }

    companion object {
        private const val ARG_IMAGE_URI = "ARG_IMAGE_URI"

        fun getInstance(imageUri: String): Fragment {
            return ImageViewerFragment().apply {
                arguments = bundleOf(ARG_IMAGE_URI to imageUri)
            }
        }
    }
}