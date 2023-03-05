package com.aldidwikip.mygithubuser.presentation.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.databinding.ActivityFavoriteBinding
import com.aldidwikip.mygithubuser.helper.ItemTouch
import com.aldidwikip.mygithubuser.presentation.adapter.FavoriteAdapter
import com.aldidwikip.mygithubuser.presentation.ui.BaseVBActivity
import com.aldidwikip.mygithubuser.presentation.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavoriteActivity : BaseVBActivity<ActivityFavoriteBinding>() {
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    @Inject
    lateinit var favoriteAdapter: FavoriteAdapter

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
                val listFavoriteUser = favoriteAdapter.currentList[position]
                favoriteViewModel.deleteFavorite(listFavoriteUser)
                Snackbar.make(binding.rvListFavorite, "${listFavoriteUser.username} ${getString(R.string.removed_favorite)}", Snackbar.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvListFavorite)

        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.rvListFavorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            addItemDecoration(itemDecoration)
            adapter = favoriteAdapter
        }

        favoriteAdapter.setOnItemClickCallback { userDetail ->
            Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_USER, userDetail.username)
            }.also { startActivity(it) }
        }
    }

    private fun subscribeData() {
        lifecycleScope.launch {
            favoriteViewModel.userFavorites.flowWithLifecycle(lifecycle).collect {
                favoriteAdapter.submitList(it)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return true
    }
}