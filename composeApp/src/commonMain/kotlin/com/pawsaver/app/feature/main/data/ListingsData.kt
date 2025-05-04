package com.pawsaver.app.feature.main.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListingsResponse(
    val count: Int,
    val next: String? = null,
    val previous: String? = null,
    val results: List<Listing>,
) {
    @Serializable
    data class Listing(
        val id: Int,
        val name: String,
        val type: AnimalType,
        val breed: String,
        val gender: Gender,
        @SerialName("birth_date")
        val birthDate: String,
        val weight: Double,
        val description: String,
        @SerialName("listing_type")
        val listingType: ListingType,
        val status: String,
        @SerialName("last_seen_location")
        val lastSeenLocation: String?,
        @SerialName("last_seen_date")
        val lastSeenDate: String?,
        @SerialName("listing_date")
        val listingDate: String,
        val age: Int
    )

    @Serializable
    enum class AnimalType {
        @SerialName("dog")
        DOG,

        @SerialName("cat")
        CAT,

        @SerialName("parrot")
        PARROT,

        @SerialName("turtle")
        TURTLE,

        @SerialName("rabbit")
        RABBIT,

        @SerialName("fish")
        FISH,

        @SerialName("hamster")
        HAMSTER,

        @SerialName("other")
        OTHER
    }


    @Serializable
    enum class Gender {
        @SerialName("male")
        MALE,

        @SerialName("female")
        FEMALE
    }

    @Serializable
    enum class ListingType {
        @SerialName("adoption")
        ADOPTION,

        @SerialName("lost")
        LOST
    }
}