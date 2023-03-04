package com.aldidwikip.mygithubuser.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<ENTITY>(diffUtil: DiffUtil.ItemCallback<ENTITY>) :
        ListAdapter<ENTITY, BaseAdapter.BaseViewHolder<ENTITY>>(diffUtil) {

    abstract fun itemClickCallback(item: ENTITY)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ENTITY> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)
        return BaseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ENTITY>, position: Int) {
        with(holder) {
            bind(getItem(position))
            itemView.setOnClickListener {
                itemClickCallback(getItem(adapterPosition))
            }
        }
    }

    class BaseViewHolder<T>(private val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            binding.apply {
                setVariable(BR.userData, item)
                executePendingBindings()
            }
        }
    }
}