package com.example.snappy.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.snappy.base.PrivateSharedPrefManager
import com.example.snappy.data.APIResult.Companion.loading
import com.example.snappy.data.model.PetsDetail
import com.example.snappy.data.model.api_model.LoginRequest
import com.example.snappy.data.model.api_model.LoginResponse
import com.example.snappy.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    @Inject
    lateinit var privateSharedPreferencesManager: PrivateSharedPrefManager

    var bannerImageUrl = arrayListOf(
        "https://picsum.photos/id/237/200/300",
        "https://picsum.photos/id/237/200/300",
        "https://picsum.photos/id/237/200/300"
    )

    private val _petsList = MutableLiveData<ArrayList<PetsDetail>>()
    val petsList: LiveData<ArrayList<PetsDetail>> = _petsList

    fun loadDummyData() {
        val dummyList: ArrayList<PetsDetail> = arrayListOf()
        repeat(8) {
            dummyList.add(PetsDetail(
                "https://picsum.photos/id/237/200/300",
                "Cute dog",
                "2",
                "50",
                "Delhi",
                "9876543210"
            ))
        }

        _petsList.value = dummyList
    }

    private val _responseLogin = MutableLiveData<com.example.snappy.data.APIResult<LoginResponse>>()
    val responseLogin: LiveData<com.example.snappy.data.APIResult<LoginResponse>> = _responseLogin

    suspend fun login(user: String, pass: String) {
        val request = LoginRequest(user, pass)
        _responseLogin.postValue(loading(null))
        val res = repository.login(request)
        _responseLogin.postValue(res)
    }
}