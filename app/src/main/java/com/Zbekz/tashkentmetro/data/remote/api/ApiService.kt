package com.Zbekz.tashkentmetro.data.remote.api

import com.Zbekz.tashkentmetro.domain.model.register.User
import retrofit2.http.POST

interface ApiService {

    @POST("auth/auth")
    suspend fun getUsers(): List<User>
}