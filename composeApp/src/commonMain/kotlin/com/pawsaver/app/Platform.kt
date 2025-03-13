package com.pawsaver.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform