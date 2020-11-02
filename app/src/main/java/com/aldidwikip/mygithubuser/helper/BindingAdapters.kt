package com.aldidwikip.mygithubuser.helper

import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.aldidwikip.mygithubuser.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("app:img_from_url")
fun imgFromUrl(circleImageView: CircleImageView, url: String?) {
    circleImageView.load(url) {
        placeholder(R.drawable.ic_person)
        fallback(R.drawable.ic_person)
        error(R.drawable.ic_person)
    }
}

@BindingAdapter("app:string_plurals")
fun stringPlurals(repository: TextView, num: Int) {
    val plurals = repository.context.resources.getQuantityString(R.plurals.plural_repository, num, num)
    repository.text = plurals
}