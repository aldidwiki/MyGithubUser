package com.aldidwikip.mygithubuser.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.adapter.SectionsPagerAdapter
import com.aldidwikip.mygithubuser.data.User
import com.aldidwikip.mygithubuser.databinding.ActivityDetailBinding
import com.aldidwikip.mygithubuser.helper.DataState
import com.aldidwikip.mygithubuser.helper.showLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_USER = "extra_user"
    }

    private val detailViewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra(EXTRA_USER) as String
        subscribeData(username)
    }

    private fun subscribeData(username: String) {
        detailViewModel.getUser(username)
        detailViewModel.user.observe(this, { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    progress_bar.showLoading(false)
                    appendData(dataState.data)
                    setupTabLayout(username)
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
                company = company
        )
    }

    private fun setupTabLayout(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tab_follows.setupWithViewPager(view_pager)

        supportActionBar?.elevation = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}