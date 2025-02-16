package com.platformcommons.app.ui.movies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.snackbar.Snackbar
import com.platformcommons.app.R
import com.platformcommons.app.databinding.FragmentMoviesDetailsBinding
import com.platformcommons.app.domain.movies.MoviesDetailsResponse
import com.platformcommons.app.ui.movies.viewmodel.MoviesViewModel
import com.platformcommons.app.utils.Constants.TEMP_IMAGE_URL
import com.platformcommons.app.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class MoviesDetailsFragment : Fragment() {

    private var _binding: FragmentMoviesDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: MoviesDetailsFragmentArgs by navArgs()

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.getMoviesDetails(args.movieId)
            initViewModels()
        }
    }

    private fun initViewModels() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.movieDetails.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                ).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            progressCircular.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            response.data?.let { movieData ->
                                setupUI(movieData)
                            }
                            progressCircular.visibility = View.GONE
                        }

                        is Resource.Error -> {
                            response.message?.let { message ->
                                Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show()
                            }
                            progressCircular.visibility = View.GONE
                            movieDescriptionTv.visibility = View.GONE
                            movieDescLinearLayout.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun setupUI(moviesDetails: MoviesDetailsResponse) {
        binding.apply {
            moviesDetails.apply {

                poster_path.let {
                    Glide.with(root).load("$TEMP_IMAGE_URL$it")
                        .transition(DrawableTransitionOptions.withCrossFade()).into(movieImage)
                }

                movieDescriptionTv.visibility = View.VISIBLE
                movieTitle.text = title
                movieDescLinearLayout.visibility = View.VISIBLE
                rateTv.text = root.context.getString(R.string.rate_format_str, vote_average)
                movieDescription.text = this.overview

                val date =
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(release_date)?.let {
                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(
                            it
                        )
                    }
                movieReleaseDate.text = requireContext().getString(R.string.release_date, date)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}