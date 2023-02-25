package com.aldidwikip.mygithubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.UsersAdapter
import com.aldidwikip.mygithubuser.data.model.Users
import com.aldidwikip.mygithubuser.databinding.ActivityMainBinding
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.showLoading
import com.aldidwikip.mygithubuser.ui.BaseVBActivity
import com.aldidwikip.mygithubuser.ui.detail.DetailActivity
import com.aldidwikip.mygithubuser.ui.favorite.FavoriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseVBActivity<ActivityMainBinding>(), UsersAdapter.OnItemClickCallback {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private var doubleTapOnce = false
    private lateinit var usersAdapter: UsersAdapter

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun init(savedInstanceState: Bundle?) {
        subscribeData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        usersAdapter = UsersAdapter(R.layout.list_user)
        usersAdapter.setOnItemClickCallback(this)
        binding.rvListUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(itemDecoration)
            adapter = usersAdapter
        }
    }

    private fun subscribeData() {
        binding.apply {
            mainViewModel.users.observe(this@MainActivity) { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        progressBar.showLoading(false)
                        usersAdapter.submitList(dataState.data)
                    }
                    is DataState.Error -> {
                        progressBar.showLoading(false)
                        Log.e(TAG, "subscribeData: ${dataState.exception.message}")
                        Toast.makeText(this@MainActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> {
                        progressBar.showLoading(true)
                    }
                }
            }

            mainViewModel.searchUser.observe(this@MainActivity) { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        progressBar.showLoading(false)
                        if (dataState.data.isEmpty()) Toast.makeText(this@MainActivity, getString(R.string.no_result_found), Toast.LENGTH_SHORT).show()
                        else usersAdapter.submitList(dataState.data)
                    }
                    is DataState.Error -> {
                        progressBar.showLoading(false)
                        Log.e(TAG, "subscribeData: ${dataState.exception.message}")
                        Toast.makeText(this@MainActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> progressBar.showLoading(true)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = getString(R.string.search_user)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.getSearchUser(query!!)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressCallback() {
        if (doubleTapOnce) {
            super.onBackPressCallback()
            return
        }
        this.doubleTapOnce = true
        Toast.makeText(this, getString(R.string.double_tap_to_exit), Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            delay(2000)
            doubleTapOnce = false
        }
    }

    override fun onItemClicked(users: Users) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, users.username)
            startActivity(this)
        }
    }
}