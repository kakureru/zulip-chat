package com.example.courcework.app.presentation.screens.chat.attachment

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.ImageLoader
import coil.load
import com.example.courcework.app.di.attachment.DaggerAttachmentComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.databinding.FragmentAttachmentBinding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

class AttachmentFragment : Fragment() {

    private val uri: Uri by lazy { arguments?.getParcelable<Uri>(ARG_URI) ?: throw IllegalArgumentException() }
    private val resultKey: String by lazy { arguments?.getString(ARG_RESULT_KEY) ?: throw IllegalArgumentException() }

    @Inject lateinit var imageLoader: ImageLoader
    @Inject lateinit var attachmentViewModelFactory: AttachmentViewModelFactory.Factory
    private val vm: AttachmentViewModel by viewModels { attachmentViewModelFactory.create(uri, uri.mimeType(), resultKey) }

    private lateinit var binding: FragmentAttachmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerAttachmentComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAttachmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            image.load(uri)
            btnSend.setOnClickListener {
                vm.accept(AttachmentEvent.SendMessage(textMessage.text.toString()))
            }
            btnClose.setOnClickListener {
                vm.accept(AttachmentEvent.BackPressed)
            }
        }
        vm.effect.handleEffect()
    }

    private fun SharedFlow<AttachmentEffect>.handleEffect() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@handleEffect.collect { effect ->
                when (effect) {
                    is AttachmentEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun Uri.mimeType(): String? =
        if (ContentResolver.SCHEME_CONTENT == this.scheme)
            requireContext().contentResolver.getType(this)
        else
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(this.toString()).lowercase(Locale.getDefault())
            )

    companion object {
        private const val ARG_URI = "ARG_URI"
        private const val ARG_RESULT_KEY = "ARG_RESULT_KEY"

        fun getInstance(uri: Uri, resultKey: String): Fragment = AttachmentFragment().apply {
            arguments = bundleOf(
                ARG_URI to uri,
                ARG_RESULT_KEY to resultKey
            )
        }
    }
}