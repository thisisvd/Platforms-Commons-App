package com.platformcommons.app.ui.users

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.platformcommons.app.R
import com.platformcommons.app.data.local.model.User
import com.platformcommons.app.databinding.FragmentAddUserBinding
import com.platformcommons.app.ui.users.viewmodel.UsersViewModel
import com.platformcommons.app.core.NetworkUtils
import com.platformcommons.app.core.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddUserFragment : Fragment() {

    private var _binding: FragmentAddUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsersViewModel by viewModels()

    @Inject
    lateinit var networkUtils: NetworkUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            // Set up the transition
            sharedElementEnterTransition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_element_transaction)
            addUserLayout.transitionName = "shared_element_container"

            addUser.setOnClickListener {
                if (!userNameTv.text.isNullOrEmpty() && !userJobTv.text.isNullOrEmpty()) {
                    viewModel.addUser(
                        User(
                            name = userNameTv.text.toString(),
                            job = userJobTv.text.toString(),
                        )
                    )
                } else {
                    Snackbar.make(root, "Enter all fields!", Snackbar.LENGTH_SHORT).show()
                }
            }

            initViewModels()
        }
    }

    private fun initViewModels() {
        binding.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.userDetails.flowWithLifecycle(
                    viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED
                ).collect { response ->
                    when (response) {
                        is Resource.Loading -> {
                            progressCircular.visibility = View.VISIBLE
                        }

                        is Resource.Success -> {
                            response.data?.let {
                                Snackbar.make(
                                    root, "Successful User Created!", Snackbar.LENGTH_LONG
                                ).show()
                            }
                            progressCircular.visibility = View.GONE
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                progressCircular.visibility = View.GONE
                                if (!networkUtils.isNetworkAvailable()) {
                                    Snackbar.make(
                                        root,
                                        "Saved locally due to No Network!",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        else -> {}
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