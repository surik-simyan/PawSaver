package com.pawsaver.app.core.di

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual val settingsModule = module {
    single<Settings> {
        SharedPreferencesSettings(
            androidApplication().getSharedPreferences("tokens", Context.MODE_PRIVATE)
        )
    }
}