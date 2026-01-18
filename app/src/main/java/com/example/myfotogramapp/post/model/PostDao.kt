package com.example.myfotogramapp.post.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PostDao {

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("SELECT id FROM PostEntity")
    suspend fun getAllPostIds(): List<Int>

    @Query("SELECT * FROM PostEntity WHERE id = :id")
    suspend fun getPostById(id: Int): PostEntity?

    @Query("SELECT * FROM PostEntity WHERE authorId = :authorId ORDER BY createdAt DESC")
    suspend fun getPostsByAuthorId(authorId: Int): List<PostEntity>

    @Query("DELETE FROM PostEntity")
    suspend fun clearPost()

}