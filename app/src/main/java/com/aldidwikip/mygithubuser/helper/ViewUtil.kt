package com.aldidwikip.mygithubuser.helper

import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.aldidwikip.mygithubuser.R

fun ProgressBar.showLoading(state: Boolean) {
    if (state) this.visibility = View.VISIBLE else this.visibility = View.GONE
}

fun MenuItem.favorite(isFavorite: Boolean) {
    this.setIcon(
            if (isFavorite) R.drawable.ic_favorite
            else R.drawable.ic_favorite_border
    )
}