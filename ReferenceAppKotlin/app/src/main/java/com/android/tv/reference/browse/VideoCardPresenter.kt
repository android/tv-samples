/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.tv.reference.browse

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.leanback.widget.Presenter
import com.android.tv.reference.R
import com.android.tv.reference.databinding.PresenterVideoCardBinding
import com.android.tv.reference.shared.datamodel.Video
import com.android.tv.reference.shared.datamodel.VideoType
import com.squareup.picasso.Picasso

/**
 * Presents a [Video] as an [ImageCardView] with descriptive text based on the Video's type.
 */
class VideoCardPresenter : Presenter() {
    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val context = parent.context
        val binding = PresenterVideoCardBinding.inflate(LayoutInflater.from(context), parent, false)

        // Set the image size ahead of time since loading can take a while.
        val resources = context.resources
        binding.root.setMainImageDimensions(
                resources.getDimensionPixelSize(R.dimen.image_card_width),
                resources.getDimensionPixelSize(R.dimen.image_card_height))

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any?) {
        checkNotNull(item)
        val video = item as Video
        val binding = PresenterVideoCardBinding.bind(viewHolder.view)
        binding.root.titleText = video.name
        binding.root.contentText = getContentText(binding.root.resources, video)

        Picasso.get().load(video.thumbnailUri).placeholder(R.drawable.image_placeholder)
                .error(R.drawable.image_placeholder).into(binding.root.mainImageView)
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val binding = PresenterVideoCardBinding.bind(viewHolder.view)
        binding.root.mainImage = null
    }

    /**
     * Returns a string to display as the "content" for an [ImageCardView].
     *
     * Since Watch Next behavior differs for episodes, movies, and clips, this string makes it
     * more clear which [VideoType] each [Video] is. For example, clips are never included in
     * Watch Next.
     */
    private fun getContentText(resources: Resources, video: Video): String {
        return when (video.videoType) {
            VideoType.EPISODE -> resources.getString(R.string.content_type_season_episode,
                    video.seasonNumber, video.episodeNumber)
            VideoType.MOVIE -> resources.getString(R.string.content_type_movie)
            VideoType.CLIP -> resources.getString(R.string.content_type_clip)
        }
    }
}
