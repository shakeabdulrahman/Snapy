package com.example.snappy.data.repository

import android.util.Log
import com.example.snappy.data.ApiService
import com.example.snappy.data.BaseRemoteDataSource
import com.example.snappy.data.APIResult
import com.example.snappy.data.model.api_model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(private val service: ApiService) : BaseRemoteDataSource() {

    suspend fun login(request: LoginRequest): APIResult<LoginResponse> {
        val response = service.login(request)
        Log.d("LOGIN RESPONSE : ", response.toString())
        return getResult { response }
    }
}