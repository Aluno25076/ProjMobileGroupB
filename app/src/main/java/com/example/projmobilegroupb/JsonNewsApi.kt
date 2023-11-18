package com.example.projmobilegroupb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface JsonNewsApi {
    @GET("api/news")
    fun getNewsItem(): Call<List<NewsItem>>
}