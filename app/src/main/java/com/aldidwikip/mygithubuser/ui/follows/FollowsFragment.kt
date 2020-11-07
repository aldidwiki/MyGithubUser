package com.aldidwikip.mygithubuser.ui.follows

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.UsersAdapter
import com.aldidwikip.mygithubuser.data.model.Users
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.showLoading
import com.aldidwikip.mygithubuser.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_follows.*

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

    private val followsViewModel: FollowsViewModel by viewModels()
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_follows, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_SECTION_USERNAME) as String
        val index = arguments?.getInt(ARG_SECTION_INDEX) as Int

        followsViewModel.getFollows(username)

        subscribeData(index)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        usersAdapter = UsersAdapter(R.layout.list_follows)
        usersAdapter.setOnItemClickCallback(this)
        rv_follows.apply {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(itemDecoration)
            adapter = usersAdapter
            messageView = adaptive_message_view
        }
    }

    private fun subscribeData(index: Int) {
        when (index) {
            0 -> followsViewModel.userFollowing.observe(viewLifecycleOwner, { appendData(it) })
            1 -> followsViewModel.userFollowers.observe(viewLifecycleOwner, { appendData(it) })
        }
    }

    private fun appendData(dataState: DataState<List<Users>>) {
        when (dataState) {
            is DataState.Success -> {
                progress_bar.showLoading(false)
                usersAdapter.submitList(dataState.data)
            }
            is DataState.Error -> {
                progress_bar.showLoading(false)
                Log.e(TAG, "appendData: ${dataState.exception.message}")
            }
            is DataState.Loading -> {
                progress_bar.showLoading(true)
                adaptive_message_view.visibility = View.INVISIBLE
            }
        }
    }

    override fun onItemClicked(users: Users) {
        Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, users.username)
            startActivity(this)
        }
    }
}