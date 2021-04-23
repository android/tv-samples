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

import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.DividerRow
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import com.android.tv.reference.shared.datamodel.VideoGroup

class BrowseAdapter(videoGroup: List<VideoGroup>, customMenus: List<BrowseCustomMenu>) :
    ArrayObjectAdapter(ListRowPresenter()) {
    init {
        addVideoGroups(videoGroup)
        add(DividerRow())
        addCustomMenus(customMenus)
    }

    private fun addVideoGroups(videoGroup: List<VideoGroup>) {
        val cardPresenter = VideoCardPresenter()
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
