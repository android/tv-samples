package com.example.android.tvleanback.presenter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.example.android.tvleanback.databinding.IconRowHeaderBinding

import com.example.android.tvleanback.interfaces.HasHeader

class IconRowHeaderPresenter : RowHeaderPresenter() {
    class ViewHolder(val binding: IconRowHeaderBinding) : RowHeaderPresenter.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = IconRowHeaderBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        require(viewHolder is ViewHolder)
        require(item is HasHeader)
        item.header.iconRes?.let {
            val icon: Drawable =
                viewHolder.view.resources.getDrawable(it, null)
            viewHolder.binding.icon.setImageDrawable(icon)
        }
    }
}