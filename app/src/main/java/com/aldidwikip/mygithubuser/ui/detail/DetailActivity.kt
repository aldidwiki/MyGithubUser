package com.aldidwikip.mygithubuser.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.SectionsPagerAdapter
import com.aldidwikip.mygithubuser.data.model.User
import com.aldidwikip.mygithubuser.data.model.UserProperty
import com.aldidwikip.mygithubuser.databinding.ActivityDetailBinding
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.favorite
import com.aldidwikip.mygithubuser.helper.showLoading
import com.aldidwikip.mygithubuser.ui.BaseVBActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class DetailActivity : BaseVBActivity<ActivityDetailBinding>() {

    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_USER = "extra_user"
    }

    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite = false
    private lateinit var username: String

    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        username = intent.getStringExtra(EXTRA_USER) as String
        subscribeData(username)

        isFavorite = runBlocking {
            withContext(IO) { detailViewModel.isFavorite(username) }
        }
    }

    private fun subscribeData(username: String) {
        binding.apply {
            detailViewModel.getUser(username)
            detailViewModel.user.observe(this@DetailActivity) { dataState ->
                when (dataState) {
                    is DataState.Success -> {
                        progressBar.showLoading(false)
                        try {
                            appendData(dataState.data)
                            setupTabLayout(username)
                        } catch (e: NullPointerException) {
                            Log.e(TAG, "subscribeData: ${e.message}")
                            Toast.makeText(this@DetailActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                        }
                    }
                    is DataState.Error -> {
                        progressBar.showLoading(false)
                        Log.e(TAG, "subscribeData: ${dataState.exception.message}")
                        Toast.makeText(this@DetailActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    }
                    is DataState.Loading -> progressBar.showLoading(true)
                }
            }
        }
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
        binding.apply {
            viewPager.adapter = sectionsPagerAdapter
            tabFollows.setupWithViewPager(viewPager)
        }

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
                    Snackbar.make(binding.viewDetail, getString(R.string.marked_favorite), Snackbar.LENGTH_SHORT).show()
                } else {
                    detailViewModel.deleteFavorite(username)
                    Snackbar.make(binding.viewDetail, getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }
}