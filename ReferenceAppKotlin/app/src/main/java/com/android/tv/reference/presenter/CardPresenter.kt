package com.android.tv.reference.presenter

import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.android.tv.reference.R
import com.android.tv.reference.shared.datamodel.Video
import com.squareup.picasso.Picasso

class CardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
        val mContext = parent!!.context
        val cardView = ImageCardView(mContext)
        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true

        val cardResources = cardView.resources
        val height = cardResources.getDimensionPixelSize(R.dimen.lb_basic_card_main_height)
        val width = cardResources.getDimensionPixelSize(R.dimen.lb_basic_card_main_width)
        cardView.setMainImageDimensions(width, height)

        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder?, item: Any?) {
        val video = item as Video
        val cardView = viewHolder!!.view as ImageCardView
        cardView.titleText = video.name

        Picasso.get().load(video.thumbnailUri).placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder).into(cardView.mainImageView)

    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder?) {
        val cardView = viewHolder!!.view as ImageCardView

        cardView.badgeImage = null
        cardView.mainImage = null
    }
}