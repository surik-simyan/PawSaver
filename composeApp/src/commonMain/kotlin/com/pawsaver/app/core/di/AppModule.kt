package com.pawsaver.app.core.di

import com.pawsaver.app.core.network.PawsaverApi
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val appModule = module {
    single<Settings> { Settings() }
    single<PawsaverApi> { PawsaverApi(get()) }
}