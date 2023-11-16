package com.example.projmobilegroupb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonListApi {

    @GET("api/symbols")
    fun getList(): Call<List<String>>
}