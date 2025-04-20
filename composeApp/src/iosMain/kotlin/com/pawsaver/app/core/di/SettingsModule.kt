package com.pawsaver.app.core.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

actual val settingsModule = module {
    single<Settings> {
        NSUserDefaultsSettings(
            NSUserDefaults.standardUserDefaults
        )
    }
}