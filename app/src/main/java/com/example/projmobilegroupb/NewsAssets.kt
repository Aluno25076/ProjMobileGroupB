package com.example.projmobilegroupb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NewsAssets: Fragment() {

    private lateinit var jsonNewsApi: JsonNewsApi

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.news_assets_layout, container, false)

        val buttonBack = view.findViewById<TextView>(R.id.backMonitoredAssets)

        buttonBack.setOnClickListener {
            val nextFrag = MonitoredAssets()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val jsonNewsApi = AssetClient.retrofit.create(JsonNewsApi::class.java)
        val call = jsonNewsApi.getNewsItem()

        call.enqueue(object : Callback<List<NewsItem>> {
            override fun onResponse(
                call: Call<List<NewsItem>>,
                response: Response<List<NewsItem>>
            ) {
                if (response.isSuccessful) {
                    val newsList: List<NewsItem> = response.body() ?: emptyList()
                    displayNews(newsList)
                } else {
                    //TRatar erros na resposta
                }
            }

            override fun onFailure(call: Call<List<NewsItem>>, t: Throwable) {
                //Tratar falha na chamada
            }
        })
    }

    private fun displayNews(newsList: List<NewsItem>) {
        val news = view?.findViewById<TextView>(R.id.newsContainer)

        if (news != null) {
            val newsText = StringBuilder()

            newsList.forEach { newsItem ->
                newsText.append("Title: ${newsItem.title}\n")
                newsText.append("Description: ${newsItem.description}\n")
                newsText.append("Date: ${newsItem.date}\n")
                newsText.append("Image: ${newsItem.imageurl}\n")

            }
            news.text = newsText.toString()
        }

    }
}
