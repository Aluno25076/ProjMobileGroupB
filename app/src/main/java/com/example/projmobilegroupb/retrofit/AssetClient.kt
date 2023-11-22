package com.example.projmobilegroupb.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AssetClient {

    val retrofit = Retrofit.Builder().baseUrl("http://tuxdroid.pythonanywhere.com").addConverterFactory(
        GsonConverterFactory.create()).build()

}