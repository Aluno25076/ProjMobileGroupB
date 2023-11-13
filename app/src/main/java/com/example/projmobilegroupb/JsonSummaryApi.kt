package com.example.projmobilegroupb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface JsonSummaryApi {

    @GET("summary/{symbol}")
    fun getSummary(@Path("symbol") summaryId: Int): Call<Symbol>
}