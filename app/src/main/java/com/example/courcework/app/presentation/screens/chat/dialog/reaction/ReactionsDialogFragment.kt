package com.example.courcework.app.presentation.screens.chat.dialog.reaction

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import com.example.courcework.app.presentation.screens.chat.custom.dp
import com.example.courcework.app.presentation.screens.chat.custom.setSpanCountByColumnWidth
import com.example.courcework.app.presentation.screens.chat.model.EmojiItem
import com.example.courcework.databinding.ReactionBottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

typealias ReactionsDialogListener = (reaction: EmojiItem) -> Unit

class ReactionsDialogFragment : DialogFragment() {

    private val emojiSet: List<EmojiItem>
        get() = requireArguments().getParcelableArrayList(ARG_EMOJI_SET) ?: throw IllegalArgumentException()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = ReactionBottomSheetDialogBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialogBinding.listReactions.setSpanCountByColumnWidth(16f.dp(requireContext()))
        val adapter = ReactionAdapter {
            parentFragmentManager.setFragmentResult(
                REQUEST_KEY,
                bundleOf(KEY_REACTION_RESPONSE to it)
            )
            dismiss()
        }
        dialogBinding.listReactions.adapter = adapter
        adapter.submitList(emojiSet)
        return dialog
    }

    companion object {
        private val TAG = ReactionsDialogFragment::class.java.simpleName
        private const val ARG_EMOJI_SET = "ARG_EMOJI_SET"
        private const val REQUEST_KEY = "REQUEST_KEY"
        private const val KEY_REACTION_RESPONSE = "KEY_REACTION_RESPONSE"

        fun show(manager: FragmentManager, emojiSet: List<EmojiItem>) {
            val dialogFragment = ReactionsDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_EMOJI_SET to emojiSet)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(
            manager: FragmentManager,
            lifecycleOwner: LifecycleOwner,
            listener: ReactionsDialogListener
        ) {
            manager.setFragmentResultListener(
                REQUEST_KEY,
                lifecycleOwner,
                FragmentResultListener { _, result ->
                    listener.invoke(result.getParcelable(KEY_REACTION_RESPONSE)!!)
                }
            )
        }
    }
}