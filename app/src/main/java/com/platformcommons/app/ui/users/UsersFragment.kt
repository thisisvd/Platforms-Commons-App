package com.platformcommons.app.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.platformcommons.app.R
import com.platformcommons.app.databinding.FragmentUsersBinding
import com.platformcommons.app.ui.users.pagination.UsersPagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {

    // view binding
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!

    // paging adapter
    private lateinit var usersPagingAdapter: UsersPagingAdapter

    // view models
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // will call user api once
        viewModel.getUsersList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            initViewModels()
            setupUsersList()
            fab.setOnClickListener {
                val extras = FragmentNavigatorExtras(fab to "shared_element_container")
                findNavController().navigate(
                    R.id.action_usersFragment_to_addUserFragment, null, null, extras
                )
            }
        }
    }

    // initialized view model
    private fun initViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userList.flowWithLifecycle(
                viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
            ).collect { response ->
                response?.let {
                    usersPagingAdapter.submitData(response)
                }
            }

        }
    }

    // setup adapter lists
    private fun setupUsersList() {
        binding.apply {
            usersPagingAdapter = UsersPagingAdapter()
            recyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = usersPagingAdapter
            }
            usersPagingAdapter.apply {
                setOnItemClickListener {
                    findNavController().navigate(R.id.action_usersFragment_to_moviesFragment)
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