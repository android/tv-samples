package com.example.android.tvleanback.presenter

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.leanback.widget.RowPresenter

class IconRowPresenter : RowPresenter() {
    init {
        headerPresenter = IconRowHeaderPresenter()
    }

    override fun createRowViewHolder(parent: ViewGroup): ViewHolder {
        return ViewHolder(FrameLayout(parent.context))
    }

    override fun isUsingDefaultSelectEffect() = false
}