package com.android.tv.reference.browse

import androidx.leanback.widget.*
import com.android.tv.reference.presenter.CardPresenter
import com.android.tv.reference.presenter.CustomMenuItemPresenter
import com.android.tv.reference.shared.datamodel.VideoGroup

class BrowseAdapter(videoGroup: List<VideoGroup>, customMenus: List<BrowseCustomMenu>) :
    ArrayObjectAdapter(ListRowPresenter()) {
    init {
        addVideoGroups(videoGroup)
        addCustomMenus(customMenus)
    }

    private fun addVideoGroups(videoGroup: List<VideoGroup>) {
        val cardPresenter = CardPresenter()
        videoGroup.forEach {
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            listRowAdapter.addAll(0, it.videoList)
            val headerItem = HeaderItem(it.category)
            add(ListRow(headerItem, listRowAdapter))
        }
    }

    private fun addCustomMenus(customMenus: List<BrowseCustomMenu>) {
        val presenter = CustomMenuItemPresenter()
        customMenus.forEach { menu ->
            val listRowAdapter = ArrayObjectAdapter(presenter)
            listRowAdapter.addAll(0, menu.menuItems)
            val headerItem = HeaderItem(menu.title)
            add(ListRow(headerItem, listRowAdapter))
        }
    }
}
