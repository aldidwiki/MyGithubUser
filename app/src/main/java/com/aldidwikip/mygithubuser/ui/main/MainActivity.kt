package com.aldidwikip.mygithubuser.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.UsersAdapter
import com.aldidwikip.mygithubuser.data.model.Users
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.showLoading
import com.aldidwikip.mygithubuser.ui.detail.DetailActivity
import com.aldidwikip.mygithubuser.ui.favorite.FavoriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), UsersAdapter.OnItemClickCallback {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mainViewModel: MainViewModel by viewModels()
    private var doubleTapOnce = false
    private lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        subscribeData()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        usersAdapter = UsersAdapter(R.layout.list_user)
        usersAdapter.setOnItemClickCallback(this)
        rv_list_user.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(itemDecoration)
            adapter = usersAdapter
            messageView = adaptive_message_view
        }
    }

    private fun subscribeData() {
        mainViewModel.users.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    progress_bar.showLoading(false)
                    usersAdapter.submitList(dataState.data)
                }
                is DataState.Error -> {
                    progress_bar.showLoading(false)
                    Log.e(TAG, "subscribeData: ${dataState.exception.message}")
                    Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> {
                    progress_bar.showLoading(true)
                    adaptive_message_view.visibility = View.INVISIBLE
                }
            }
        })

        mainViewModel.searchUser.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    progress_bar.showLoading(false)
                    if (dataState.data.isEmpty()) Toast.makeText(this, getString(R.string.no_result_found), Toast.LENGTH_SHORT).show()
                    else usersAdapter.submitList(dataState.data)
                }
                is DataState.Error -> {
                    progress_bar.showLoading(false)
                    Log.e(TAG, "subscribeData: ${dataState.exception.message}")
                    Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                }
                is DataState.Loading -> progress_bar.showLoading(true)
            }
        })
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
            R.id.action_language -> Intent(Settings.ACTION_LOCALE_SETTINGS).also {
                startActivity(it)
            }
            R.id.action_favorite -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (doubleTapOnce) {
            super.onBackPressed()
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