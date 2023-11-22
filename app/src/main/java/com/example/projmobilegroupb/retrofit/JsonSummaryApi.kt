package com.example.projmobilegroupb.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonSummaryApi {

    @GET("api/symbol/summary/{symbol}")
    fun getSummary(@Path("symbol") summary: String): Call<Symbol>
}