package com.android.tv.reference.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import com.android.tv.reference.databinding.PresenterMenuItemBinding

/**
 * Presenter for displaying text in a grid, useful for custom menus and navigation.
 */
class CustomMenuItemPresenter() : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        val binding = PresenterMenuItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        item: Any?
    ) {
        checkNotNull(item)
        val binding = PresenterMenuItemBinding.bind(viewHolder.view)
        binding.root.text = item.toString()
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
}
