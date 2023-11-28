package com.example.projmobilegroupb.retrofit

import com.example.projmobilegroupb.dataClasses.Detail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonDetailsApi {
    @GET("api/symbol/details/{symbol}")
    fun getDetails(@Path("symbol") detail: String): Call<Detail>
}