package com.aldidwikip.mygithubuser.util

import android.view.View
import android.widget.ProgressBar

fun ProgressBar.showLoading(state: Boolean) {
    if (state) this.visibility = View.VISIBLE else this.visibility = View.GONE
}