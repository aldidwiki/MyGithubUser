package com.aldidwikip.mygithubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.data.Users
import com.aldidwikip.mygithubuser.databinding.ListUserBinding

class UsersAdapter : ListAdapter<Users, UsersAdapter.UsersViewModel>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewModel {
        val view: ListUserBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.list_user, parent, false)
        return UsersViewModel(view)
    }

    override fun onBindViewHolder(holder: UsersViewModel, position: Int) {
        with(holder) {
            bind(getItem(position))
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(getItem(adapterPosition))
            }
        }
    }

    inner class UsersViewModel(private val binding: ListUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(users: Users) {
            binding.apply {
                userData = users
                executePendingBindings()
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(users: Users)
    }

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
}