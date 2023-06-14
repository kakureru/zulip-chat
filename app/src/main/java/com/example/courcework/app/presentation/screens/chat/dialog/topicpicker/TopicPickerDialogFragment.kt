package com.example.courcework.app.presentation.screens.chat.dialog.topicpicker

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.courcework.app.di.chat.DaggerChatComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.ViewModelFactory
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.databinding.TopicPickerBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias TopicPickerDialogListener = (topicId: TopicId) -> Unit

class TopicPickerDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val vm: TopicPickerViewModel by viewModels { viewModelFactory }
    private lateinit var binding: TopicPickerBottomSheetDialogBinding
    private lateinit var topicPickAdapter: TopicPickAdapter

    private val currentTopicId: TopicId by lazy {
        arguments?.getParcelable<TopicId>(ARG_CURRENT_TOPIC_ID) ?: throw IllegalArgumentException()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChatComponent.factory().create(context.getAppComponent()).inject(this)
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        binding = TopicPickerBottomSheetDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        topicPickAdapter = TopicPickAdapter {
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(KEY_TOPIC_RESPONSE to it)
            )
            dismiss()
        }
        binding.listTopics.adapter = topicPickAdapter
        vm.dialogState.render()
        vm.dialogEffect.handleEffect()
        vm.accept(TopicPickerEvent.LoadTopics(currentTopicId))
        return dialog
    }

    private fun SharedFlow<TopicPickerState>.render() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@render.collect { state ->
                binding.noTopicsPlaceholder.isVisible = state.data.isEmpty()
                topicPickAdapter.submitList(state.data)
            }
        }
    }

    private fun SharedFlow<TopicPickerEffect>.handleEffect() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@handleEffect.collect { effect ->
                when (effect) {
                    is TopicPickerEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private val TAG = TopicPickerDialogFragment::class.java.simpleName
        private const val REQUEST_KEY = "ARG_REQUEST_KEY"
        private const val KEY_TOPIC_RESPONSE = "KEY_TOPIC_RESPONSE"
        private const val ARG_CURRENT_TOPIC_ID = "ARG_CURRENT_TOPIC_ID"

        fun show(manager: FragmentManager, currentTopicId: TopicId) =
            TopicPickerDialogFragment().apply {
                arguments = bundleOf(ARG_CURRENT_TOPIC_ID to currentTopicId)
            }.show(manager, TAG)

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: TopicPickerDialogListener
        ) {
            manager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner,
                FragmentResultListener { _, result ->
                    listener.invoke(result.getParcelable(KEY_TOPIC_RESPONSE)!!)
                }
            )
        }
    }
}