package com.example.projmobilegroupb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class NewsAdapter(private val context: Context, private val newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
            val descriptionTextView: TextView = view.findViewById(R.id.textViewDescription)
            val dateTextView: TextView = view.findViewById(R.id.textViewDate)
            val imageView: ImageView = view.findViewById(R.id.imageView)

        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_card_layout, parent, false)
        return ViewHolder(view)
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val newsItem = newsList[position]
            holder.titleTextView.text = "${newsItem.title}\n"
            holder.descriptionTextView.text = "${newsItem.description}\n"
            holder.dateTextView.text = "${newsItem.date}\n"

            Glide.with(context).load(newsItem.image_url).into(holder.imageView)

    }

    override fun getItemCount(): Int {
        return newsList.size


    }

    }