


package com.example.firstapp.feature.home.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("email")
    val email: String = "",

    @SerializedName("createdAt")
    val createdAt: String = ""
)

data class UserResponse(
    @SerializedName("status")
    val status: Boolean = false,

    @SerializedName("message")
    val message: String = "",

    @SerializedName("users")
    val users: List<User> = emptyList()
)