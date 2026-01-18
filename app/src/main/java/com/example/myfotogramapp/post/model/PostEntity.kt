package com.example.myfotogramapp.post.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class PostEntity (
    @PrimaryKey val id: Int,
    val authorId: Int,
    val createdAt: String,
    val contentPicture: String,
    val contentText: String,
    @Embedded val location: LocationPostEntity? = null
)

data class LocationPostEntity (
    val latitude: Double,
    val longitude: Double
)

@Serializable
data class PostDto (
    val id: Int,
    val authorId: Int,
    val createdAt: String,
    val contentPicture: String,
    val contentText: String,
    val location: LocationDto? = null
)

@Serializable
data class LocationDto(
    val latitude: Double?,
    val longitude: Double?
)

fun PostDto.toEntity(): PostEntity {
    return PostEntity(
        id = id,
        authorId = authorId,
        createdAt = createdAt,
        contentPicture = contentPicture,
        contentText = contentText,
        location = location?.let {
            if (it.latitude != null && it.longitude != null) {
                LocationPostEntity(
                    latitude = it.latitude,
                    longitude = it.longitude
                )
            } else {
                null
            }
        }
    )
}

@Serializable
data class NewPost(
    val contentText: String,
    val contentPicture: String,
    val location: LocationDto? = null
)

@Serializable
data class PostReactions(
    val hearts: Int,
    val likes: Int,
    val dislikes: Int
)

@Serializable
data class ReactionToPost(
    val reaction: String
)