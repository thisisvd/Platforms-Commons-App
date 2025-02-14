package com.platformcommons.app.ui.movies.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.platformcommons.app.R
import com.platformcommons.app.databinding.UserItemUiBinding
import com.platformcommons.app.model.Data
import com.platformcommons.app.model.movies.MoviesResult

class MoviesPagingAdapter :
    PagingDataAdapter<MoviesResult, MoviesPagingAdapter.ViewHolder>(MoviesListDiffCallback()) {

    class MoviesListDiffCallback : DiffUtil.ItemCallback<MoviesResult>() {
        override fun areItemsTheSame(oldItem:MoviesResult, newItem:MoviesResult) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem:MoviesResult, newItem: MoviesResult
        ) = oldItem == newItem

        override fun getChangePayload(oldItem:MoviesResult, newItem:MoviesResult): Any? {
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
        fun bind(movieData: MoviesResult?, onItemClickListener: ((Int) -> Unit)?) {
            binding.apply {
                movieData?.let { movie ->
//                    user.avatar.let {
//                        Glide.with(root).load(user.avatar)
//                            .transition(DrawableTransitionOptions.withCrossFade()).into(avatarImage)
//                    }

                    userNameTv.text = movie.title

                    root.setOnClickListener {
                        onItemClickListener?.let { it(movie.id) }
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
    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }
}