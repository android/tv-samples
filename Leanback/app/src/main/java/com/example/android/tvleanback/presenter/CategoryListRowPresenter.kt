package com.example.android.tvleanback.presenter

import android.view.ViewGroup
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.RowHeaderPresenter
import androidx.leanback.widget.RowPresenter

class CategoryListRowPresenter : ListRowPresenter() {
    init {
        headerPresenter = RowHeaderPresenter()
        syncActivatePolicy = SYNC_ACTIVATED_TO_EXPANDED_AND_SELECTED
    }

    override fun createRowViewHolder(parent: ViewGroup): RowPresenter.ViewHolder {
        return (super.createRowViewHolder(parent) as ViewHolder).apply {
            with(gridView) {
                windowAlignmentOffsetPercent = 0f
                val typedArray = parent.context.obtainStyledAttributes(
                    intArrayOf(androidx.leanback.R.styleable.LeanbackTheme_browsePaddingStart)
                )
                windowAlignmentOffset = typedArray.getDimensionPixelSize(
                    androidx.leanback.R.styleable.LeanbackTheme_browsePaddingStart,
                    0
                )
                typedArray.recycle()
                itemAlignmentOffsetPercent = 0f
                clipChildren = false
            }
        }
    }

    override fun isUsingDefaultShadow() = false
}