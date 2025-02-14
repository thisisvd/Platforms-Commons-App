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
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.platformcommons.app.R
import com.platformcommons.app.databinding.FragmentMoviesBinding
import com.platformcommons.app.ui.movies.pagination.MoviesPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var moviesPagingAdapter: MoviesPagingAdapter

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initViewModels()
            setupUsersList()
        }
    }

    private fun initViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMoviesList().flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { response ->
                moviesPagingAdapter.submitData(response)
            }
        }
    }

    private fun setupUsersList() {
        binding.apply {
            moviesPagingAdapter = MoviesPagingAdapter()
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = moviesPagingAdapter
            }
            moviesPagingAdapter.apply {
                setOnItemClickListener { id ->
                    val bundle = Bundle().apply {
                        putInt("movieId", id)
                    }
                    findNavController().navigate(
                        R.id.action_moviesFragment_to_moviesDetailsFragment,
                        bundle
                    )
                }
                addLoadStateListener { loadState ->
                    when (loadState.refresh) {
                        is LoadState.Loading -> {
                            progressCircular.visibility = View.VISIBLE
                        }

                        is LoadState.Error -> {
                            progressCircular.visibility = View.GONE
                        }

                        is LoadState.NotLoading -> {
                            progressCircular.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}