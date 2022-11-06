package com.example.android.tvleanback.data

import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ObjectAdapter
import com.example.android.tvleanback.interfaces.HasHeader

class HeaderListRow(
    id: Long,
    override val header: HeaderItemRow,
    val dataAdapter: ObjectAdapter
) : ListRow(id, null, dataAdapter), HasHeader