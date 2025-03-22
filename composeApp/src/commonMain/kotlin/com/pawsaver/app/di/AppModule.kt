package com.pawsaver.app.di

import com.pawsaver.app.network.PawsaverApi
import org.koin.dsl.module

val appModule = module {
    single<PawsaverApi> { PawsaverApi() }
}