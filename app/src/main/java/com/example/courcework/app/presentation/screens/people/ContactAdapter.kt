package com.example.courcework.app.presentation.screens.people

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.courcework.R
import com.example.courcework.databinding.ContactItemBinding
import com.example.courcework.domain.model.Presence

class ContactAdapter(private val onClick: (contactId: Int) -> Unit) :
    ListAdapter<ContactItem, ContactAdapter.ContactViewHolder>(DiffCallback) {

    inner class ContactViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                onClick(getItem(adapterPosition).id)
            }
        }

        fun bind(item: ContactItem) {
            binding.apply {
                name.text = item.name
                email.text = item.email
                image.load(item.avatarUrl) { placeholder(R.drawable.ic_face) }
                status.setStatus(item.presence)
            }
        }

        private fun ImageView.setStatus(presence: Presence) {
            when(presence) {
                Presence.OFFLINE -> setColorFilter(Color.RED)
                Presence.IDLE -> setColorFilter(Color.YELLOW)
                Presence.ACTIVE -> setColorFilter(Color.GREEN)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ContactViewHolder(ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ContactItem>() {
            override fun areItemsTheSame(oldItem: ContactItem, newItem: ContactItem) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: ContactItem, newItem: ContactItem) = oldItem == newItem
        }
    }
}