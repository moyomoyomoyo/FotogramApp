package com.example.myfotogramapp.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity
data class UserEntity (
    @PrimaryKey val id: Int,
    val createdAt: String,
    val username: String,
    val bio: String,
    val dateOfBirth: String,
    val profilePicture: String,
    val isYourFollower: Boolean,
    val isYourFollowing: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int
)

@Serializable
data class UserDto(
    val id: Int,
    val createdAt: String,
    val username: String?= null,
    val bio: String? = null,
    val dateOfBirth: String? = null,
    val profilePicture: String? = null,
    val isYourFollower: Boolean,
    val isYourFollowing: Boolean,
    val followersCount: Int,
    val followingCount: Int,
    val postsCount: Int
)

fun UserDto.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        createdAt = createdAt,
        username = username ?: "unknown",
        bio = bio ?: "",
        dateOfBirth = dateOfBirth ?: "",
        profilePicture = profilePicture ?: "",
        isYourFollower = isYourFollower,
        isYourFollowing = isYourFollowing,
        followersCount = followersCount,
        followingCount = followingCount,
        postsCount = postsCount
    )
}


@Serializable
data class UserPicUpdateDto (
    val base64: String
)

@Serializable
data class UserInfoUpdateDto (
    val username: String,
    val bio: String = "",
    val dateOfBirth: String = ""
)

@Serializable
data class SessionDataDto(
    val sessionId: String,
    val userId: Int
)
