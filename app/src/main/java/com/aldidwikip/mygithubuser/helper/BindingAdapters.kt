package com.aldidwikip.mygithubuser.helper

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.aldidwikip.mygithubuser.R
import com.bumptech.glide.Glide

@BindingAdapter("app:img_from_url")
fun imgFromUrl(imageView: ImageView, url: String?) {
    Glide.with(imageView)
            .load(url)
            .placeholder(R.drawable.ic_person)
            .error(R.drawable.ic_person)
            .fallback(R.drawable.ic_person)
            .into(imageView)
}

@BindingAdapter("app:string_plurals")
fun stringPlurals(repository: TextView, num: Int) {
    val plurals = repository.context.resources.getQuantityString(R.plurals.plural_repository, num, num)
    repository.text = plurals
}