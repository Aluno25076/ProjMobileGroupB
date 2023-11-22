package com.example.projmobilegroupb.retrofit

import retrofit2.Call
import retrofit2.http.GET


interface JsonNewsApi {
    @GET("api/news")
    fun getNewsItem(): Call<List<NewsItem>>
}