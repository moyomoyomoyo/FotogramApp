package com.example.myfotogramapp.user.repository

import android.content.Context
import android.util.Log
import com.example.myfotogramapp.application.database.DatabaseBuilder
import com.example.myfotogramapp.auth.AuthManager
import com.example.myfotogramapp.network.ApiRoutes
import com.example.myfotogramapp.user.model.SessionDataDto
import com.example.myfotogramapp.user.model.UserDto
import com.example.myfotogramapp.user.model.UserEntity
import com.example.myfotogramapp.user.model.UserInfoUpdateDto
import com.example.myfotogramapp.user.model.UserPicUpdateDto
import com.example.myfotogramapp.user.model.toEntity
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class UserRepository(context: Context, private val client: HttpClient) {

    private val db = DatabaseBuilder.getInstance(context)
    private val userDao = db.userDao()

    // funzione crea utente che salva la sessione con auhtmanager
    suspend fun createUser(authManager: AuthManager): Boolean{
        return try {
            val session = client.post(ApiRoutes.createUser()).body<SessionDataDto>()
            Log.i("KtorDemo", "Created user with id: ${session.userId}")

            authManager.saveSession(
                sessionId = session.sessionId,
                userId = session.userId
            )

            true
        } catch (e: Exception){
            Log.e("KtorDemo", "Errore durante la creazione utente: ${e.message}", e)
            false
        }
    }

    // funzione per aggiornare le info utente
    suspend fun updateUserInfo(update: UserInfoUpdateDto): Boolean {

        return try{
            val response = client.put(ApiRoutes.createUser()) {
                setBody(update)
            }

            val userUpdated = response.body<UserDto>()
            userDao
            Log.i("KtorDemo", "Utente aggiornato: ${userUpdated.id}")


            true
        } catch (e: Exception){
            Log.e("KtorDemo", "Errore durante l'aggiornamento utente: ${e.message}", e)
            false
        }

    }

    // funzione per aggiornare la foto profilo
    suspend fun updateUserProfilePicture(picture: String): Boolean {
        return try{
            val updatePic = UserPicUpdateDto(base64 = picture)

            val response = client.put(ApiRoutes.updatePicture()) {
                setBody(updatePic)
            }

            val userUpdated = response.body<UserDto>()
            userDao.insertUser(userUpdated.toEntity())

            Log.i("KtorDemo", "Foto profilo aggiornata per utente: ${userUpdated.id}")

            true
        } catch (e: Exception){
            Log.e("KtorDemo", "Errore durante l'aggiornamento foto profilo: ${e.message}", e)
            false
        }
    }

    // funzione che recupera user passato l'id
    suspend fun getUserById(userId: Int): UserEntity? {
        return try{
            val response = client.get(ApiRoutes.getUser(userId))
            Log.i("KtorDemo", "Utente recuperato: $userId")

            val user = response.body<UserDto>().toEntity()
            userDao.insertUser(user)

            user
        } catch (e: Exception) {
            Log.e("KtorDemo", "Errore durante il recupero utente con id $userId: ${e.message}", e)
            null
        }
    }

    // funzione per fare follow ad un utente
    suspend fun follow(targetId: Int): Boolean {
        return try {
            val response = client.put(ApiRoutes.followUser(targetId))
            Log.i("KtorDemo", "Following id $targetId: ${response.status}")
            true
        } catch (e: Exception) {
            Log.e("KtorDemo", "Errore durante il follow dell'utente con id $targetId: ${e.message}", e)
            false
        }
    }

    // funzione per fare unfollow ad un utente
    suspend fun unfollow(targetId: Int): Boolean {
        return try {
            val response = client.put(ApiRoutes.unfollowUser(targetId))
            Log.i("KtorDemo", "Unfollowing id $targetId: ${response.status}")
            true
        } catch (e: Exception) {
            Log.e("KtorDemo", "Errore durante il unfollow dell'utente con id $targetId: ${e.message}", e)
            false
        }
    }

}
