package com.example.snappy.data.model.api_model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("data") var data: UserData? = null,
) : BaseResponse() {
    inner class UserData {
        @SerializedName("agentId")
        var agentId: Int? = null

        @SerializedName("agentName")
        var agentName: String? = null

        @SerializedName("userName")
        var userName: String? = null

        @SerializedName("password")
        var password: String? = null
    }
}
