package com.aldidwikip.mygithubuser.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.databinding.ListFavoriteBinding
import com.aldidwikip.mygithubuser.domain.model.UserDetail
import javax.inject.Inject

class FavoriteAdapter @Inject constructor() : ListAdapter<UserDetail, FavoriteAdapter.FavoriteViewHolder>(DIFF_UTIL) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ListFavoriteBinding>(inflater, R.layout.list_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(private val binding: ListFavoriteBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: UserDetail) {
            binding.userData = item
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(item)
            }
        }
    }

    private var onItemClickCallback: OnItemClickCallback? = null
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun interface OnItemClickCallback {
        fun onItemClicked(item: UserDetail)
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<UserDetail>() {
            override fun areItemsTheSame(oldItem: UserDetail, newItem: UserDetail): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: UserDetail, newItem: UserDetail): Boolean {
                return oldItem == newItem
            }
        }
    }
}