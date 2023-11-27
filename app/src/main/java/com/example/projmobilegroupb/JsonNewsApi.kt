package com.example.projmobilegroupb

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

// Definição da interface JsonNewsApi
interface JsonNewsApi {
    // Anotação @GET para requisição GET HTTP
    @GET("api/news")
    // A função getNewsItem() retorna um objeto Call que contém uma lista de NewsItem
    fun getNewsItem(): Call<List<NewsItem>>
}