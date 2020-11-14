package com.aldidwikip.mygithubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.FavoriteAdapter
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.helper.ItemTouch
import com.aldidwikip.mygithubuser.ui.detail.DetailActivity
import com.aldidwikip.mygithubuser.util.provider.FavoriteProvider.Companion.CONTENT_URI
import com.aldidwikip.mygithubuser.util.widget.ImageBannerWidget
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_favorite.*

@AndroidEntryPoint
class FavoriteActivity : AppCompatActivity(), FavoriteAdapter.OnItemClickedCallback {
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
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
                contentResolver.delete(CONTENT_URI, null, null) // only for update Uri
                Snackbar.make(rv_list_favorite, "$username ${getString(R.string.removed_favorite)}", Snackbar.LENGTH_SHORT).show()
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(rv_list_favorite)

        val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        favoriteAdapter = FavoriteAdapter()
        favoriteAdapter.setOnItemClickCallback(this)
        rv_list_favorite.apply {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            addItemDecoration(itemDecoration)
            adapter = favoriteAdapter
            messageView = adaptive_message_view
        }
    }

    private fun subscribeData() {
        favoriteViewModel.favoriteList.observe(this, { userFav ->
            favoriteAdapter.submitList(userFav.map { it.user })
            ImageBannerWidget.updateStackView(this)
        })
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