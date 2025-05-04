package com.pawsaver.app.core.data

import kotlinx.serialization.Serializable

@Serializable
class GpsPosition(
    val latitude: Double,
    val longitude: Double
)