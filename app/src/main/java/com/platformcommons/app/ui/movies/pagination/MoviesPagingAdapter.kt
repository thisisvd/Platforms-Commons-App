package com.platformcommons.app.ui.movies.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.platformcommons.app.R
import com.platformcommons.app.databinding.MovieItemUiBinding
import com.platformcommons.app.domain.movies.MoviesResult
import com.platformcommons.app.utils.Constants.TEMP_IMAGE_URL
import java.text.SimpleDateFormat
import java.util.Locale

class MoviesPagingAdapter :
    PagingDataAdapter<MoviesResult, MoviesPagingAdapter.ViewHolder>(MoviesListDiffCallback()) {

    class MoviesListDiffCallback : DiffUtil.ItemCallback<MoviesResult>() {
        override fun areItemsTheSame(oldItem: MoviesResult, newItem: MoviesResult) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: MoviesResult, newItem: MoviesResult
        ) = oldItem == newItem

        override fun getChangePayload(oldItem: MoviesResult, newItem: MoviesResult): Any? {
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

    class ViewHolder private constructor(private val binding: MovieItemUiBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movieData: MoviesResult?, onItemClickListener: ((Int) -> Unit)?) {
            binding.apply {
                movieData?.let { movie ->
                    movie.poster_path.let {
                        Glide.with(root).load("$TEMP_IMAGE_URL$it")
                            .transition(DrawableTransitionOptions.withCrossFade()).into(movieImage)
                    }

                    movieName.text = movie.title

                    val date = SimpleDateFormat(
                        "yyyy-MM-dd", Locale.getDefault()
                    ).parse(movie.release_date)?.let {
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(
                            it
                        )
                    }
                    movieReleaseDate.text = root.context.getString(R.string.release_date, date)

                    root.setOnClickListener {
                        onItemClickListener?.let { it(movie.id) }
                    }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemUiBinding.inflate(layoutInflater, parent, false)
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