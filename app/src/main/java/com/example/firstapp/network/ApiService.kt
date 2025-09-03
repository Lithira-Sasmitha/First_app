

package com.example.firstapp.network

import com.example.firstapp.feature.home.model.UserResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("user/getAllUser")
    suspend fun getAllUsers(): Response<UserResponse>
}