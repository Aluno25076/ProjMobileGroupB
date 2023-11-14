package com.example.projmobilegroupb

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AssetClient {

    val retrofit = Retrofit.Builder().baseUrl("https://tuxdroid.pythonanywhere.com/").addConverterFactory(
        GsonConverterFactory.create()).build()

}