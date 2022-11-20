package com.example.snappy.data

import android.content.Context
import com.example.snappy.data.model.api_model.LoginRequest
import com.example.snappy.data.model.api_model.LoginResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("lottery/api/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    /** -----------------------------------------------------------------------------------     */


    companion object {
        private const val BASE_URL = "http://3.110.220.93/"

        fun create(context: Context): ApiService {
            val client = OkHttpClient.Builder()
                .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
