package com.azamovhudstc.tashkentmetro.data.remote.api

import com.azamovhudstc.tashkentmetro.domain.model.register.User
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/auth")
    suspend fun getUsers(): List<User>
}