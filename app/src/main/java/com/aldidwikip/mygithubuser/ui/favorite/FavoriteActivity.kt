package com.aldidwikip.mygithubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.FavoriteAdapter
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.databinding.ActivityFavoriteBinding
import com.aldidwikip.mygithubuser.helper.ItemTouch
import com.aldidwikip.mygithubuser.ui.BaseVBActivity
import com.aldidwikip.mygithubuser.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteActivity : BaseVBActivity<ActivityFavoriteBinding>(), FavoriteAdapter.OnItemClickedCallback {
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun getViewBinding(): ActivityFavoriteBinding {
        return ActivityFavoriteBinding.inflate(layoutInflater)
    }

    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.fav_users_title)

        subscribeData()
        initRecycler()
    }

    private fun initRecycler() {
        val swipeHandler = object : ItemTouch() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val listFavoriteUser = favoriteAdapter.currentList.toMutableList().removeAt(position)
                val username = listFavoriteUser.username
                favoriteViewModel.deleteFavorite(username)
                Snackbar.make(binding.rvListFavorite, "$username ${getString(R.string.removed_favorite)}", Snackbar.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvListFavorite)

        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.setOnItemClickCallback(this)
        binding.rvListFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            addItemDecoration(itemDecoration)
            adapter = favoriteAdapter
        }
    }

    private fun subscribeData() {
        favoriteViewModel.favoriteList.observe(this) { userFav ->
            favoriteAdapter.submitList(userFav.map { it.user })
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClicked(user: User) {
        Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_USER, user.username)
            startActivity(this)
        }
    }
}