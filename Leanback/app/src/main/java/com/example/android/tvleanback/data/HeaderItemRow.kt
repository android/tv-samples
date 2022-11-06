package com.example.android.tvleanback.data

import androidx.annotation.DrawableRes

data class HeaderItemRow(
    val id: Long,
    @DrawableRes val iconRes: Int?,
    val nameRes: String
)