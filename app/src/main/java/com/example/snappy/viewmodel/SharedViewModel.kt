package com.example.snappy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snappy.base.PrivateSharedPrefManager
import com.example.snappy.data.APIResult.Companion.loading
import com.example.snappy.data.model.api_model.LoginRequest
import com.example.snappy.data.model.api_model.LoginResponse
import com.example.snappy.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

    private val _responseLogin = MutableLiveData<com.example.snappy.data.APIResult<LoginResponse>>()
    val responseLogin: LiveData<com.example.snappy.data.APIResult<LoginResponse>> = _responseLogin

    suspend fun login(user: String, pass: String) {
        val request = LoginRequest(user, pass)
        _responseLogin.postValue(loading(null))
        val res = repository.login(request)
        _responseLogin.postValue(res)
    }
}