package com.aldidwiki.consumerapp.ui

import android.database.ContentObserver
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.aldidwiki.consumerapp.R
import com.aldidwiki.consumerapp.adapter.FavoriteAdapter
import com.aldidwiki.consumerapp.data.db.DatabaseContract
import com.aldidwiki.consumerapp.data.entity.UserProperty
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FavoriteAdapter.OnItemClickCallback {
    private lateinit var favoriteAdapter: FavoriteAdapter
    private val mainViewModel: MainViewModel by viewModels()

    private val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            mainViewModel.loadFavUser()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            contentResolver.registerContentObserver(DatabaseContract.UserColumns.CONTENT_URI, true, observer)
            subscribeData()
            initRecycler()
        } catch (e: SecurityException) {
            Log.e(TAG, "onCreate: ${e.message}")
            Toast.makeText(this, getString(R.string.main_app_not_installed), Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(observer)
    }

    private fun initRecycler() {
        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.setOnItemClickCallback(this)
        rv_list_favorite.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            addItemDecoration(itemDecoration)
            adapter = favoriteAdapter
            messageView = adaptive_message_view
        }
    }

    private fun subscribeData() {
        mainViewModel.loadFavUser()
        progress_bar.visibility = View.VISIBLE
        adaptive_message_view.visibility = View.INVISIBLE
        mainViewModel.favUser.observe(this, {
            progress_bar.visibility = View.GONE
            favoriteAdapter.submitList(it)
        })
    }

    override fun onItemClicked(item: UserProperty) {
        Toast.makeText(this, "${item.username} clicked", Toast.LENGTH_SHORT).show()
    }
}