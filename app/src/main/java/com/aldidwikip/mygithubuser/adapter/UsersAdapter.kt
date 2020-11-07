package com.aldidwikip.mygithubuser.adapter

import androidx.recyclerview.widget.DiffUtil
import com.aldidwikip.mygithubuser.data.model.Users

class UsersAdapter(private val layoutId: Int) : BaseAdapter<Users>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Users>() {
            override fun areItemsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: Users, newItem: Users): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun getItemViewType(position: Int): Int = layoutId

    override fun itemClickCallback(item: Users) {
        onItemClickCallback.onItemClicked(item)
    }

    interface OnItemClickCallback {
        fun onItemClicked(users: Users)
    }
}