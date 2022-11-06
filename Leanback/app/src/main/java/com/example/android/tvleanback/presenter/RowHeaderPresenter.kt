package com.example.android.tvleanback.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.RowHeaderPresenter
import com.example.android.tvleanback.databinding.TextRowHeaderBinding
import com.example.android.tvleanback.interfaces.HasHeader

class RowHeaderPresenter : RowHeaderPresenter() {
    class ViewHolder(
        val binding: TextRowHeaderBinding
    ) : RowHeaderPresenter.ViewHolder(binding.root as View)

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = TextRowHeaderBinding.inflate(layoutInflater, parent, false)
        val viewHolder = ViewHolder(binding)
        setSelectLevel(viewHolder, 0f)
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        require(viewHolder is ViewHolder)
        require(item is HasHeader)
        val headerName = item.header.nameRes
        viewHolder.binding.rowHeader.text = headerName
    }
}