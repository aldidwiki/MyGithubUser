package com.aldidwiki.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aldidwiki.consumerapp.R
import com.aldidwiki.consumerapp.data.entity.UserProperty
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_favorite.view.*

class FavoriteAdapter : ListAdapter<UserProperty, FavoriteAdapter.FavoriteViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.FavoriteViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(getItem(adapterPosition))
            }
        }
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userProperty: UserProperty) {
            with(itemView) {
                tv_name.text = userProperty.username
                Glide.with(img_photo)
                        .load(userProperty.avatar)
                        .into(img_photo)
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(item: UserProperty)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserProperty>() {
            override fun areItemsTheSame(oldItem: UserProperty, newItem: UserProperty): Boolean {
                return oldItem.username == newItem.username
            }

            override fun areContentsTheSame(oldItem: UserProperty, newItem: UserProperty): Boolean {
                return oldItem == newItem
            }
        }
    }
}