package com.android.tv.classics.presenters

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import coil.api.load
import com.android.tv.classics.models.TvMediaMetadata
import com.android.tv.classics.utils.TvLauncherUtils

/** Default height in DP used for card presenters, larger than this results in rows overflowing */
const val DEFAULT_CARD_HEIGHT: Int = 400

/** [Presenter] used to display a metadata item as an image card */
class TvMediaMetadataPresenter(private val cardHeight: Int = DEFAULT_CARD_HEIGHT) : Presenter() {

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) = Unit

    override fun onCreateViewHolder(parent: ViewGroup) =
            Presenter.ViewHolder(ImageCardView(parent.context).apply {
                isFocusable = true
                isFocusableInTouchMode = true
                // Set card background to dark gray while image loads
                setBackgroundColor(Color.DKGRAY)
                // Do not display text under the card image
                infoVisibility = View.GONE
            })

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        // Cast item as a MediaMetadataCompat and viewholder's view as TextView
        val metadata = item as TvMediaMetadata
        val card = viewHolder.view as ImageCardView

        // Computes the card width from the given height and metadata aspect ratio
        val cardWidth = TvLauncherUtils.parseAspectRatio(metadata.artAspectRatio).let {
            cardHeight * it.numerator / it.denominator
        }

        card.titleText = metadata.title
        card.contentDescription = metadata.title
        card.setMainImageDimensions(cardWidth, cardHeight)
        card.mainImageView.load(metadata.artUri)
    }
}
