package com.example.myfotogramapp.network

object ApiRoutes {

    private const val BASE_URL = "https://server2526-main.vercel.app"

    //------- USER
    fun createUser() = "$BASE_URL/user"
    fun getUser(userId: Int) = "$BASE_URL/user/$userId"
    fun updatePicture() = "$BASE_URL/user/image"
    // user follow
    fun followUser(targetId: Int) = "$BASE_URL/follow/$targetId"
    fun unfollowUser(targetId: Int) = "$BASE_URL/follow/$targetId"

    //------- POST
    fun createPost() = "$BASE_URL/post"
    fun getPost(id: Int) = "$BASE_URL/post/$id"
    fun getPostsByAuthor(authorId: Int) = "$BASE_URL/post/list/$authorId"
    fun getFeed() = "$BASE_URL/feed"

    //------- SALVATAGGIO POST
    fun savePost() = "$BASE_URL/exercise_1/saved"
    fun getSavedPosts() = "$BASE_URL/exercise_1/saved"
}