package com.android.tv.reference.browse

import androidx.leanback.widget.*
import com.android.tv.reference.presenter.CardPresenter
import com.android.tv.reference.shared.datamodel.VideoGroup

class BrowseAdapter(videoGroup: List<VideoGroup>) : ArrayObjectAdapter() {

    init {
        this.presenterSelector = ArrayObjectAdapter(ListRowPresenter()).presenterSelector

        videoGroup.forEach {
            val listRowAdapter = ArrayObjectAdapter(CardPresenter())
            listRowAdapter.addAll(0, it.videoList)
            val headerItem = HeaderItem(it.category)
            add(ListRow(headerItem, listRowAdapter))
        }
    }
}