package com.pawsaver.app

import android.app.Application
import com.pawsaver.app.core.di.KoinInitializer

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        KoinInitializer(applicationContext).init()
    }
}