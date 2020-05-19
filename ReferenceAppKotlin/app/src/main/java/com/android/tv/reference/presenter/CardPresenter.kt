package com.android.tv.reference.presenter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import com.android.tv.reference.R
import com.android.tv.reference.databinding.PresenterCardBinding
import com.android.tv.reference.shared.datamodel.Video
import com.squareup.picasso.Picasso

class CardPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        val binding = PresenterCardBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        checkNotNull(item)
        val video = item as Video
        val binding = PresenterCardBinding.bind(viewHolder.view)
        binding.root.titleText = video.name

        Picasso.get().load(video.thumbnailUri).placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder).into(binding.root.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val binding = PresenterCardBinding.bind(viewHolder.view)
        binding.root.badgeImage = null
        binding.root.mainImage = null
    }
}
