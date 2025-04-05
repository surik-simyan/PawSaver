package com.pawsaver.app.core.di

import com.pawsaver.app.core.network.PawsaverApi
import org.koin.dsl.module

val appModule = module {
    single<PawsaverApi> { PawsaverApi() }
}