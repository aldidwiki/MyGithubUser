package com.aldidwikip.mygithubuser.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.data.model.User

class FavoriteAdapter : BaseAdapter<User>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = R.layout.list_favorite

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var onItemClickedCallback: OnItemClickedCallback

    fun setOnItemClickCallback(onItemClickedCallback: OnItemClickedCallback) {
        this.onItemClickedCallback = onItemClickedCallback
    }

    override fun itemClickCallback(item: User) {
        onItemClickedCallback.onItemClicked(item)
    }

    interface OnItemClickedCallback {
        fun onItemClicked(user: User)
    }
}
