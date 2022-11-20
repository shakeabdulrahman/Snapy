package com.example.snappy.data

import retrofit2.Response

/**
 * Abstract Base Data source class with error handling
 */

/** this class will load api response and covert into RESULT object */
abstract class BaseRemoteDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): APIResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return APIResult.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): APIResult<T> {
        return APIResult.error("Network call has failed for a following reason: $message")
    }

}

