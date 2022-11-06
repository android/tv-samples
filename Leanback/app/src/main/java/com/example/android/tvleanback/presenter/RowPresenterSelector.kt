package com.example.android.tvleanback.presenter

import androidx.leanback.widget.Presenter
import androidx.leanback.widget.PresenterSelector

object RowPresenterSelector : PresenterSelector() {
    private val presenter = CategoryListRowPresenter()

    override fun getPresenter(item: Any?): Presenter {
        return presenter
    }

    override fun getPresenters(): Array<Presenter> {
        return arrayOf(presenter)
    }
}