package com.example.myfotogramapp.application.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myfotogramapp.post.model.PostDao
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.user.model.UserDao
import com.example.myfotogramapp.user.model.UserEntity

@Database(entities = [UserEntity::class, PostEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
}