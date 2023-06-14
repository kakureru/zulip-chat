package com.example.courcework.app.presentation.screens.channels.stream.newstreamdialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.courcework.app.di.newstream.DaggerNewStreamComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.ViewModelFactory
import com.example.courcework.databinding.NewStreamDialogBinding
import com.example.courcework.domain.model.NewStreamParams
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewStreamDialogFragment : DialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
    private val vm: NewStreamViewModel by viewModels { viewModelFactory }

    private lateinit var binding: NewStreamDialogBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerNewStreamComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = NewStreamDialogBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(requireContext()).setView(binding.root).create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        with(binding){
            confirmButton.setOnClickListener {
                val streamParams = NewStreamParams(
                    name = streamName.text.toString(),
                    description = streamDescription.text.toString(),
                    announce = checkAnnounce.isChecked
                )
                vm.accept(NewStreamEvent.Confirm(streamParams))
            }

            dismissButton.setOnClickListener {
                dismiss()
            }
        }
        vm.dialogState.render()
        vm.dialogEffect.handleEffect()
        return dialog
    }

    private fun SharedFlow<NewStreamState>.render() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@render.collect { state ->
                if (state.isSuccess) dismiss()
                binding.loader.isVisible = state.isLoading
            }
        }
    }

    private fun SharedFlow<NewStreamEffect>.handleEffect() = lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@handleEffect.collect { effect ->
                when (effect) {
                    is NewStreamEffect.Error -> Toast.makeText(requireContext(), effect.msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private val TAG = NewStreamDialogFragment::class.java.simpleName
        fun show(fm: FragmentManager) = NewStreamDialogFragment().show(fm, TAG)
    }
}