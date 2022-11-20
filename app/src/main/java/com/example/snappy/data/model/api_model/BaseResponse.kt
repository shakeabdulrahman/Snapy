package com.example.snappy.data.model.api_model

import com.google.gson.annotations.SerializedName

open class BaseResponse {
    @SerializedName("status"  ) var status  : String? = null
    @SerializedName("message" ) var message : String? = null
}