package com.aldidwikip.mygithubuser.presentation.ui.follows

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.databinding.FragmentFollowsBinding
import com.aldidwikip.mygithubuser.domain.model.User
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.showLoading
import com.aldidwikip.mygithubuser.presentation.adapter.UsersAdapter
import com.aldidwikip.mygithubuser.presentation.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FollowsFragment : Fragment(), UsersAdapter.OnItemClickCallback {

    companion object {
        private const val TAG = "FollowsFragment"
        private const val ARG_SECTION_INDEX = "section_index"
        private const val ARG_SECTION_USERNAME = "section_username"

        fun newInstance(username: String?, index: Int): FollowsFragment {
            val args = Bundle().apply {
                putString(ARG_SECTION_USERNAME, username)
                putInt(ARG_SECTION_INDEX, index)
            }

            return FollowsFragment().apply { arguments = args }
        }
    }

    private var _binding: FragmentFollowsBinding? = null
    private val binding get() = _binding!!
    private val followsViewModel: FollowsViewModel by viewModels()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFollowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_SECTION_USERNAME) as String
        val index = arguments?.getInt(ARG_SECTION_INDEX) as Int

        followsViewModel.setUsername(username)

        subscribeData(index)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        usersAdapter = UsersAdapter(R.layout.list_follows)
        usersAdapter.setOnItemClickCallback(this)
        binding.rvFollows.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(itemDecoration)
            adapter = usersAdapter
        }
    }

    private fun subscribeData(index: Int) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                when (index) {
                    0 -> launch {
                        followsViewModel.userFollowing.collect { appendData(it) }
                    }
                    1 -> launch {
                        followsViewModel.userFollower.collect { appendData(it) }
                    }
                }
            }
        }
    }

    private fun appendData(dataState: DataState<List<User>>) {
        binding.apply {
            when (dataState) {
                is DataState.Success -> {
                    progressBar.showLoading(false)
                    usersAdapter.submitList(dataState.data)
                }
                is DataState.Error -> {
                    progressBar.showLoading(false)
                    Log.e(TAG, "appendData: ${dataState.errorMessage}")
                }
                is DataState.Loading -> {
                    progressBar.showLoading(true)
                }
            }
        }
    }

    override fun onItemClicked(user: User) {
        Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, user.username)
            startActivity(this)
        }
    }
}