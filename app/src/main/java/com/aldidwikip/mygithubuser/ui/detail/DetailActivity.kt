package com.aldidwikip.mygithubuser.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.SectionsPagerAdapter
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.databinding.ActivityDetailBinding
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.favorite
import com.aldidwikip.mygithubuser.helper.showLoading
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_USER = "extra_user"
    }

    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite = false
    private lateinit var username: String
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        username = intent.getStringExtra(EXTRA_USER) as String
        subscribeData(username)

        isFavorite = runBlocking {
            withContext(IO) { detailViewModel.isFavorite(username) }
        }
    }

    private fun subscribeData(username: String) {
        detailViewModel.getUser(username)
        detailViewModel.user.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    progress_bar.showLoading(false)
                    try {
                        appendData(dataState.data)
                        setupTabLayout(username)
                    } catch (e: NullPointerException) {
                        Log.e(TAG, "subscribeData: ${e.message}")
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    }
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

    private fun appendData(user: User) {
        supportActionBar?.title = user.username

        val location = user.location ?: getString(R.string.unknown)
        val company = user.company ?: getString(R.string.not_mentioned)

        binding.userData = User(
                username = user.username,
                name = user.name,
                avatar = user.avatar,
                followers = user.followers,
                following = user.following,
                repositoryNum = user.repositoryNum,
                location = location,
                company = company,
                id = user.id
        )
    }

    private fun setupTabLayout(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tab_follows.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val toggleFav = menu?.findItem(R.id.action_fav)
        toggleFav?.favorite(isFavorite)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.action_fav -> {
                isFavorite = !isFavorite
                item.favorite(isFavorite)
                if (isFavorite) {
                    detailViewModel.saveFavorite(UserProperty(username, true))
                    Snackbar.make(view_detail, getString(R.string.marked_favorite), Snackbar.LENGTH_SHORT).show()
                } else {
                    detailViewModel.deleteFavorite(username)
                    Snackbar.make(view_detail, getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }
}