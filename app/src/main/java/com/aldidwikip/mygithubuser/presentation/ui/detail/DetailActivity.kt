package com.aldidwikip.mygithubuser.presentation.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.databinding.ActivityDetailBinding
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.favorite
import com.aldidwikip.mygithubuser.helper.showLoading
import com.aldidwikip.mygithubuser.presentation.adapter.SectionsPagerAdapter
import com.aldidwikip.mygithubuser.presentation.ui.BaseVBActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : BaseVBActivity<ActivityDetailBinding>() {

    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_USER = "extra_user"
    }

    private val detailViewModel: DetailViewModel by viewModels()
    private var isFavorite = false
    private var userDetail: UserDetail? = null

    override fun getViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun init(savedInstanceState: Bundle?) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USER) as String
        subscribeData(username)
    }

    private fun subscribeData(username: String) {
        binding.apply {
            detailViewModel.setUsername(username)

            lifecycleScope.launch {
                detailViewModel.getUser.flowWithLifecycle(lifecycle).collect { dataState ->
                    when (dataState) {
                        is DataState.Loading -> {
                            progressBar.showLoading(true)
                        }
                        is DataState.Error -> {
                            progressBar.showLoading(false)
                            Log.e(TAG, "subscribeData: ${dataState.errorMessage}")
                            Toast.makeText(this@DetailActivity, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                        }
                        is DataState.Success -> {
                            progressBar.showLoading(false)

                            setupTabLayout(username)
                            userData = dataState.data
                            userDetail = dataState.data
                        }
                    }
                }
            }
        }
    }

    private fun setupTabLayout(username: String) {
        val tabTitles = intArrayOf(R.string.tab_title_1, R.string.tab_title_2)
        val sectionsPagerAdapter = SectionsPagerAdapter(tabTitles.size, supportFragmentManager, lifecycle)
        sectionsPagerAdapter.username = username

        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabFollows, binding.viewPager) { tab, pos ->
            tab.text = resources.getString(tabTitles[pos])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val toggleFav = menu?.findItem(R.id.action_fav)
        toggleFav?.favorite(isFavorite)
//        lifecycleScope.launch {
//            detailViewModel.getFavorite.flowWithLifecycle(lifecycle).collectLatest {
//                isFavorite = it.username.isNotEmpty()
//                toggleFav?.favorite(it.username.isNotEmpty())
//            }
//        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressCallback()
            R.id.action_fav -> {
                isFavorite = !isFavorite
                item.favorite(isFavorite)

                userDetail?.let {
                    if (isFavorite) {
                        detailViewModel.saveToFavorite(it)
                        Snackbar.make(binding.viewDetail, getString(R.string.marked_favorite), Snackbar.LENGTH_SHORT).show()
                    } else {
                        detailViewModel.deleteFavorite(it)
                        Snackbar.make(binding.viewDetail, getString(R.string.removed_favorite), Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }
        return true
    }
}