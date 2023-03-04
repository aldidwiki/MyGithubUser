package com.aldidwikip.mygithubuser.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aldidwikip.mygithubuser.domain.model.User

class UsersAdapter(private val layoutId: Int) : BaseAdapter<User>(DIFF_CALLBACK) {

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

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemViewType(position: Int): Int = layoutId

    override fun itemClickCallback(item: User) {
        onItemClickCallback.onItemClicked(item)
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}