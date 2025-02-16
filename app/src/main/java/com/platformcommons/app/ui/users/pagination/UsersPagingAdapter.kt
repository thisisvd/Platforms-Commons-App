package com.platformcommons.app.ui.users.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.platformcommons.app.R
import com.platformcommons.app.databinding.UserItemUiBinding
import com.platformcommons.app.domain.users.Data

class UsersPagingAdapter :
    PagingDataAdapter<Data, UsersPagingAdapter.ViewHolder>(UsersListDiffCallback()) {

    class UsersListDiffCallback : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Data, newItem: Data
        ) = oldItem == newItem

        override fun getChangePayload(oldItem: Data, newItem: Data): Any? {
            if (oldItem != newItem) {
                return newItem
            }
            return super.getChangePayload(oldItem, newItem)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: UserItemUiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userData: Data?, onItemClickListener: ((String) -> Unit)?) {
            binding.apply {
                userData?.let { user ->
                    user.avatar.let {
                        Glide.with(root).load(user.avatar)
                            .transition(DrawableTransitionOptions.withCrossFade()).into(avatarImage)
                    }

                    val userName = root.context.getString(
                        R.string.user_name_str, user.first_name, user.last_name
                    )

                    userNameTv.text = userName

                    root.setOnClickListener {
                        onItemClickListener?.let { it(userName) }
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = UserItemUiBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

    }

    // on click listener
    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }
}