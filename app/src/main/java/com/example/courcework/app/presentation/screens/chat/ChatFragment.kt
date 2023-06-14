package com.example.courcework.app.presentation.screens.chat

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.R
import com.example.courcework.app.di.chat.DaggerChatComponent
import com.example.courcework.app.getAppComponent
import com.example.courcework.app.presentation.adapter.DelegateListAdapter
import com.example.courcework.app.presentation.screens.channels.stream.model.TopicId
import com.example.courcework.app.presentation.screens.chat.adapter.DateDelegate
import com.example.courcework.app.presentation.screens.chat.adapter.OtherMessageDelegate
import com.example.courcework.app.presentation.screens.chat.adapter.OwnMessageDelegate
import com.example.courcework.app.presentation.screens.chat.custom.CustomMessageCallback
import com.example.courcework.app.presentation.screens.chat.dialog.action.MessageActionDialogFragment
import com.example.courcework.app.presentation.screens.chat.dialog.action.OwnMessageActionDialogFragment
import com.example.courcework.app.presentation.screens.chat.dialog.reaction.ReactionDialogData
import com.example.courcework.app.presentation.screens.chat.dialog.reaction.ReactionsDialogFragment
import com.example.courcework.app.presentation.screens.chat.dialog.topicpicker.TopicPickerDialogData
import com.example.courcework.app.presentation.screens.chat.dialog.topicpicker.TopicPickerDialogFragment
import com.example.courcework.app.presentation.screens.chat.imagegetter.ImageGetterFactory
import com.example.courcework.app.presentation.screens.chat.model.EmojiItem
import com.example.courcework.databinding.FragmentChatBinding
import com.google.android.material.snackbar.Snackbar
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.android.storeholder.LifecycleAwareStoreHolder
import vivid.money.elmslie.android.storeholder.StoreHolder
import javax.inject.Inject

@UnstableApi class ChatFragment : ElmFragment<ChatEvent, ChatEffect, ChatState>() {

    private val topicId: TopicId by lazy {
        arguments?.getParcelable<TopicId>(ARG_TOPIC_ID) ?: throw IllegalArgumentException()
    }

    @Inject lateinit var storeFactory: ChatStoreFactory.Factory
    override val storeHolder: StoreHolder<ChatEvent, ChatEffect, ChatState> = LifecycleAwareStoreHolder(lifecycle) { storeFactory.create(topicId).create() }
    override val initEvent: ChatEvent = ChatEvent.UI.Initialize

    @Inject lateinit var imageGetterFactory: ImageGetterFactory.Factory
    private val clipboardManager by lazy { requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    private lateinit var binding: FragmentChatBinding

    private val otherMessageCallback = object : CustomMessageCallback {
        override fun onLongClick(messageId: Int) = store.accept(ChatEvent.UI.OtherMessageLongClick(messageId))
        override fun onLinkClick(url: String) = store.accept(ChatEvent.UI.MessageLinkClick(url))
        override fun onReactionClick(emojiItem: EmojiItem, messageId: Int) = store.accept(ChatEvent.UI.ReactionClick(emojiItem, messageId))
        override fun onNewReactionClick(messageId: Int) = store.accept(ChatEvent.UI.AddReaction(messageId))
    }

    private val ownMessageCallback = object : CustomMessageCallback {
        override fun onLongClick(messageId: Int) = store.accept(ChatEvent.UI.OwnMessageLongClick(messageId))
        override fun onLinkClick(url: String) = store.accept(ChatEvent.UI.MessageLinkClick(url))
        override fun onReactionClick(emojiItem: EmojiItem, messageId: Int) = store.accept(ChatEvent.UI.ReactionClick(emojiItem, messageId))
        override fun onNewReactionClick(messageId: Int) = store.accept(ChatEvent.UI.AddReaction(messageId))
    }

    private val pickMedia = registerForActivityResult(PickVisualMedia()) { uri ->
        if (uri != null) {
            store.accept(ChatEvent.UI.NewAttachment(uri))
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChatComponent.factory().create(context.getAppComponent()).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        requireActivity().apply {
            if (this is AppCompatActivity) {
                setSupportActionBar(binding.toolbar)
                supportActionBar?.setDisplayShowTitleEnabled(false)
            }
        }
        return binding.root
    }

    private val imageGetterProvider = { textView: TextView -> imageGetterFactory.create(textView).create() }

    private val ownMessageDelegate = OwnMessageDelegate(
        imageGetterProvider = imageGetterProvider,
        customMessageCallback = ownMessageCallback,
    )

    private val otherMessageDelegate = OtherMessageDelegate(
        imageGetterProvider = imageGetterProvider,
        customMessageCallback = otherMessageCallback,
    )

    private val chatAdapter: DelegateListAdapter = DelegateListAdapter().apply {
        addDelegate(otherMessageDelegate)
        addDelegate(ownMessageDelegate)
        addDelegate(DateDelegate())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            topicName.text = requireContext().resources.getString(R.string.topic_name, topicId.topicName)
            listMessages.apply {
                adapter = chatAdapter
                setupScrollListener()
            }
            btnBack.setOnClickListener { store.accept(ChatEvent.UI.BackPressed) }
            btnAttach.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))
            }
            btnSendMessage.setOnClickListener {
                store.accept(ChatEvent.UI.SendMessage(content = textMessage.text.toString()))
                textMessage.text?.clear()
            }
            btnAccept.setOnClickListener {
                store.currentState.editingMessage?.let {
                    store.accept(ChatEvent.UI.MessageEdited(messageId = it.messageId, newContent = textMessage.text.toString()))
                    textMessage.text?.clear()
                }
            }
            editStatus.onCancelClick = {
                store.accept(ChatEvent.UI.CancelEdit)
                textMessage.text?.clear()
            }
            textMessage.doOnTextChanged { text, _, _, _ ->
                store.accept(ChatEvent.UI.InputChanged(text.toString()))
            }
        }
    }

    override fun render(state: ChatState) {
        with(binding) {
            loader.isVisible = state.isLoading
            listMessages.isVisible = state.data != null
            btnSendMessage.isVisible = state.isTyping && state.editingMessage == null
            btnAttach.isVisible = !state.isTyping && state.editingMessage == null
            btnAccept.isVisible = state.editingMessage != null
            editStatus.isVisible = state.editingMessage != null
            streamName.text =
                if (state.isConnectionAvailable) requireContext().resources.getString(R.string.stream_name, topicId.streamId.toString())
                else requireContext().resources.getString(R.string.waiting_for_connection)
            state.editingMessage?.let { editStatus.setEditMessage(it.content) }
            state.data?.let { chatAdapter.submitList(state.data) }
        }
    }

    override fun handleEffect(effect: ChatEffect) {
        when (effect) {
            is ChatEffect.Error -> Snackbar.make(binding.root, effect.msg, Snackbar.LENGTH_LONG).show()
            is ChatEffect.MessageSent -> with(binding.listMessages) { adapter?.let { smoothScrollToPosition(it.itemCount) } }
            is ChatEffect.EditingStarted -> binding.textMessage.setText(effect.content)
            is ChatEffect.ReactionDialog -> showReactionsDialog(effect.data)
            is ChatEffect.OtherMessageActionDialog -> showMessageActionDialog(effect.messageId)
            is ChatEffect.OwnMessageActionDialog -> showOwnMessageActionDialog(effect.messageId)
            is ChatEffect.TopicPickerDialog -> showTopicPickerDialog(effect.data)
        }
    }

    private fun showReactionsDialog(dialogData: ReactionDialogData) = ReactionsDialogFragment.apply {
        setupListener(childFragmentManager, this@ChatFragment) {
            store.accept(ChatEvent.UI.NewReactionSelected(it, dialogData.messageId))
        }
    }.show(childFragmentManager, dialogData.emojiSet)

    private fun showMessageActionDialog(messageId: Int) = MessageActionDialogFragment.apply {
        setupListener(childFragmentManager, this@ChatFragment) {
            when(it) {
                ACTION_ADD_REACTION -> store.accept(ChatEvent.UI.AddReaction(messageId))
                ACTION_COPY -> store.accept(ChatEvent.UI.CopyMessageToClipboard(messageId, clipboardManager))
            }
        }
    }.show(childFragmentManager)

    private fun showOwnMessageActionDialog(messageId: Int) = OwnMessageActionDialogFragment.apply {
        setupListener(childFragmentManager, this@ChatFragment) {
            when (it) {
                ACTION_ADD_REACTION -> store.accept(ChatEvent.UI.AddReaction(messageId))
                ACTION_COPY -> store.accept(ChatEvent.UI.CopyMessageToClipboard(messageId, clipboardManager))
                ACTION_EDIT -> store.accept(ChatEvent.UI.EditMessage(messageId))
                ACTION_CHANGE_TOPIC -> store.accept(ChatEvent.UI.ChangeMessageTopic(messageId))
                ACTION_DELETE -> store.accept(ChatEvent.UI.DeleteMessage(messageId))
            }
        }
    }.show(childFragmentManager)

    private fun showTopicPickerDialog(dialogData: TopicPickerDialogData) = TopicPickerDialogFragment.apply {
        setupListener(childFragmentManager, this@ChatFragment) {
            store.accept(ChatEvent.UI.NewTopicSelected(dialogData.messageId, it))
        }
    }.show(childFragmentManager, dialogData.currentTopicId)

    private fun RecyclerView.setupScrollListener() {
        val lm = this.layoutManager as LinearLayoutManager
        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                store.accept(ChatEvent.UI.Scroll(lm.findFirstVisibleItemPosition()))
            }
        })
    }

    companion object {
        const val ARG_TOPIC_ID = "ARG_TOPIC_ID"

        fun getInstance(topicId: TopicId): Fragment {
            return ChatFragment().apply {
                arguments = bundleOf(ARG_TOPIC_ID to topicId)
            }
        }
    }
}