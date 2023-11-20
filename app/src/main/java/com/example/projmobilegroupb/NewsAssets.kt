package com.example.projmobilegroupb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
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
                    displayNews2(newsList)
                } else {
                    //TRatar erros na resposta
                }
            }

            override fun onFailure(call: Call<List<NewsItem>>, t: Throwable) {
                //Tratar falha na chamada
            }
        })
    }


    private fun displayNews2(newsList: List<NewsItem>) {
        for ((index, newsItem) in newsList.withIndex()) {
            val cardViewId =
                resources.getIdentifier("cardView${index + 1}", "id", requireContext().packageName)
            val cardView = view?.findViewById<CardView>(cardViewId)

            cardView?.let {
                val titleTextView = it.findViewById<TextView>(
                    resources.getIdentifier(
                        "textViewTitle${index + 1}",
                        "id",
                        requireContext().packageName
                    )
                )
                val descriptionTextView = it.findViewById<TextView>(
                    resources.getIdentifier(
                        "textViewDescription${index + 1}",
                        "id",
                        requireContext().packageName
                    )
                )
                val dateTextView = it.findViewById<TextView>(
                    resources.getIdentifier(
                        "textViewDate${index + 1}",
                        "id",
                        requireContext().packageName
                    )
                )
                val imageView = it.findViewById<ImageView>(
                    resources.getIdentifier(
                        "imageView${index + 1}",
                        "id",
                        requireContext().packageName
                    )
                )

                titleTextView.text = "Title: ${newsItem.title}\n"
                descriptionTextView.text = "Description: ${newsItem.description}\n"
                dateTextView.text = "Date: ${newsItem.date}\n"

                Glide.with(requireContext()).load(newsItem.image_url).into(imageView)
            }
        }
    }
}
