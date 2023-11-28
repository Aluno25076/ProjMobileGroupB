package com.example.projmobilegroupb.retrofit

import com.example.projmobilegroupb.dataClasses.NewsItem
import retrofit2.Call
import retrofit2.http.GET

// Definição da interface JsonNewsApi
interface JsonNewsApi {
    // Anotação @GET para requisição GET HTTP
    @GET("api/news")
    // A função getNewsItem() retorna um objeto Call que contém uma lista de NewsItem
    fun getNewsItem(): Call<List<NewsItem>>
}