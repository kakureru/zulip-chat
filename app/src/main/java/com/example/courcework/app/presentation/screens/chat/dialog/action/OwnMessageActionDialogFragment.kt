package com.example.courcework.app.presentation.screens.chat.dialog.action

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.courcework.databinding.OwnMessageActionBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

typealias OwnMessageActionDialogListener = (action: Int) -> Unit

class OwnMessageActionDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = OwnMessageActionBottomSheetDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        with(dialogBinding) {
            addReaction.setOnClickResult(ACTION_ADD_REACTION)
            copy.setOnClickResult(ACTION_COPY)
            edit.setOnClickResult(ACTION_EDIT)
            changeTopic.setOnClickResult(ACTION_CHANGE_TOPIC)
            delete.setOnClickResult(ACTION_DELETE)
        }
        return dialog
    }

    private fun View.setOnClickResult(actionKey: Int) {
        setOnClickListener {
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(KEY_ACTION_RESPONSE to actionKey)
            )
            dismiss()
        }
    }

    companion object {
        private val TAG = OwnMessageActionDialogFragment::class.java.simpleName
        private const val REQUEST_KEY = "ARG_REQUEST_KEY"
        private const val KEY_ACTION_RESPONSE = "KEY_ACTION_RESPONSE"
        const val ACTION_ADD_REACTION: Int = 0
        const val ACTION_COPY: Int = 1
        const val ACTION_EDIT: Int = 2
        const val ACTION_CHANGE_TOPIC: Int = 3
        const val ACTION_DELETE: Int = 4

        fun show(manager: FragmentManager) {
            val dialogFragment = OwnMessageActionDialogFragment()
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: OwnMessageActionDialogListener
        ) {
            manager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner,
                FragmentResultListener { _, result ->
                    listener.invoke(result.getInt(KEY_ACTION_RESPONSE))
                }
            )
        }
    }
}