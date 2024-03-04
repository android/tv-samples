package com.google.jetfit

import android.app.Application
import com.google.jetfit.data.repositories.JetFitImpl
import com.google.jetfit.data.repositories.JetFitRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent

@HiltAndroidApp
class JetFitApplication : Application()
