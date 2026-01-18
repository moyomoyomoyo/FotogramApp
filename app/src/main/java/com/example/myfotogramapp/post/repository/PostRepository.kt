package com.example.myfotogramapp.post.repository

import android.content.Context
import android.util.Log
import com.example.myfotogramapp.application.database.DatabaseBuilder
import com.example.myfotogramapp.network.ApiRoutes
import com.example.myfotogramapp.post.model.NewPost
import com.example.myfotogramapp.post.model.PostDto
import com.example.myfotogramapp.post.model.PostEntity
import com.example.myfotogramapp.post.model.toEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import kotlinx.coroutines.runBlocking

class PostRepository(context: Context, private val client: HttpClient) {
    private val db = DatabaseBuilder.getInstance(context)
    private val postDao = db.postDao()

    // funzione per creare un nuovo post
    suspend fun createPost(newPost: NewPost): Boolean {
        return try {

            val response = client.post(ApiRoutes.createPost()) {
                setBody(newPost)
            }

            val postCreated = response.body<PostDto>()
            postDao.insertPost(postCreated.toEntity())
            Log.i("KtorDemo", "Post create con id: ${postCreated.id}")

            true
        } catch (e: Exception) {
            false
        }
    }

    // funzione per ottenere un post partedno da un id
    suspend fun getPostById(postId: Int): PostEntity? {
        return try {
            // controllo se ho il post in locale
            val cachedPost = postDao.getPostById(postId)
            if (cachedPost != null) {
                Log.i("KtorDemo", "Post id $postId trovato nel database locale")
                return cachedPost
            }

            // chiamata per ottenere il post
            val response = client.get(ApiRoutes.getPost(postId))
            val post = response.body<PostDto>()
            postDao.insertPost(post.toEntity())

            Log.i("KtorDemo", "Ottenuto post id: ${post.id}")

            return post.toEntity()
        } catch (e: Exception) {
            Log.e("KtorDemo", "Errore durante il recupero del post: ${e.message}", e)
            null
        }
    }

    // funzione per ottenere tutti i post di un user
    suspend fun getPostsByAuthorId(authorId: Int): List<PostEntity> {
        return try {
            // chiamata per ottenere i post
            val response = client.get(ApiRoutes.getPostsByAuthor(authorId))
            val postsId = response.body<List<Int>>()

            Log.i("KtorDemo", "ci sono ${postsId.size} post dell'user id: $authorId")

            val cachedPosts = postDao.getPostsByIds(postsId)
            // prendo  i post in cachedPosts e salvo solo gli id
            val cachedIds = cachedPosts.map { it.id }.toSet()
            // filtro gli id dei post che non ho in locale
            val missingIds = postsId.filter { it !in cachedIds }

            missingIds.forEach { postId ->
                getPostById(postId)
            }

//            for (i in postsId) {
//                getPostById(i)
//            }

            val posts = postDao.getPostsByAuthorId(authorId)
            Log.i("KtorDemo", "Ottenuti ${posts.size} post di user id: $authorId")

            return posts
        } catch (e: Exception) {
            Log.e("KtorDemo", "Errore durante il recupero dei post dell'user: ${e.message}", e)
            emptyList()
        }
    }

    // funzione per ottenere il feed
    suspend fun getFeed(maxPostId: Int? = null, limit: Int = 10): List<PostEntity> {
        try {
            val response = client.get(ApiRoutes.getFeed()) {
                url {
                    maxPostId?.let { parameters.append("maxPostId", it.toString()) }
                    parameters.append("limit", limit.toString())
//                    parameters.append("seed", "1")
                }
            }

            val postsId = response.body<List<Int>>()

            val feed = postsId.mapNotNull { postId ->
                getPostById(postId)
            }

            Log.i("KtorDemo", "Ottenuti ${feed.size} post nel feed")
            return feed
        } catch (e: Exception) {
            Log.e("KtorDemo", "Errore durante il recupero del feed: ${e.message}", e)
            return emptyList()
        }
    }

//    fun clearPostDatabase() {
//        runBlocking {
//            postDao.clearPost()
//        }
//    }

}