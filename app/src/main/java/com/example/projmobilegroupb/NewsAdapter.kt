package com.example.projmobilegroupb

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projmobilegroupb.dataClasses.NewsItem

// Classe que estende RecyclerView.Adapter para adaptar os dados à RecyclerView
class NewsAdapter(private val context: Context, private val newsList: List<NewsItem>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    // Classe que representa uma única entrada na RecyclerView
        class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
            val titleTextView: TextView = view.findViewById(R.id.textViewTitle)
            val descriptionTextView: TextView = view.findViewById(R.id.textViewDescription)
            val dateTextView: TextView = view.findViewById(R.id.textViewDate)
            val imageView: ImageView = view.findViewById(R.id.imageView)

        }
    // Método chamado quando o ViewHolder é criado
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflar o layout do item da RecyclerView
        val view = LayoutInflater.from(context).inflate(R.layout.news_card_layout, parent, false)
        // Criar e retornar uma instância do ViewHolder associado a esse layout
        return ViewHolder(view)
        }
    // Método chamado para associar os dados à View do ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
// Obter o objeto NewsItem na posição específica da lista
            val newsItem = newsList[position]
        // Configurar os elementos da View do ViewHolder com dados do NewsItem
            holder.titleTextView.text = "${newsItem.title}\n"
            holder.descriptionTextView.text = "${newsItem.description}\n"
            holder.dateTextView.text = "${newsItem.date}\n"
        // Usar a biblioteca Glide para carregar a imagem do URL no ImageView
            Glide.with(context).load(newsItem.image_url).into(holder.imageView)

    }
    // Método que retorna o número total de itens na lista
    override fun getItemCount(): Int {
        return newsList.size


    }

    }