package com.example.projmobilegroupb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//Declaração da classe NewsAssets que herda de Fragment
class NewsAssets: Fragment() {
    //Inicializa variáveis mais tarde, usando lateinit
    private lateinit var jsonNewsApi: JsonNewsApi
    private lateinit var recyclerViewNews: RecyclerView
    private lateinit var emptyCardView: CardView
    private lateinit var backButton: Button
    private lateinit var emptyTextView: TextView

    //Método chamado ao criar ou inflar a interface do fragmento
    //Faz a conexão com news_assets_layout e retorna a view.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflar o layout do fragmento
        val view = inflater.inflate(R.layout.news_assets_layout, container, false)

        //Inicializa as variáveis com base nos IDs dos elementos do layout
        recyclerViewNews = view.findViewById(R.id.recyclerViewNews)
        recyclerViewNews.layoutManager = LinearLayoutManager(context)
        emptyCardView = view.findViewById(R.id.withoutNews)
        backButton = view.findViewById(R.id.backMonitoredAssets)
        emptyTextView = view.findViewById(R.id.textViewWithoutNews)

        //Evento onClick, botão back, para regressar ao MonitoredAssets, página inicial
        backButton.setOnClickListener {
            //Cria instância do fragmento MonitoredAssets
            val nextFrag = MonitoredAssets()
            //Substituição do fragmento atual pelo novo
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    //Retorna a view do fragmento
        return view
    }

    //Quando a view é criada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    //Inicialização da interface usando Retrofit
        val jsonNewsApi = AssetClient.retrofit.create(JsonNewsApi::class.java)
        val call = jsonNewsApi.getNewsItem()
    //Faz a chamada para obter os itens de notícias
        call.enqueue(object : Callback<List<NewsItem>> {
            override fun onResponse(
                call: Call<List<NewsItem>>,
                response: Response<List<NewsItem>>
            ) {
                if (response.isSuccessful) {
                    // Se a resposta for bem-sucedida, obtem a lista de itens de notícias
                    val newsList: List<NewsItem> = response.body() ?: emptyList()
                    // Chama o método para exibir as notícias
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
    // Método privado para exibir as notícias
    private fun displayNews(newsList: List<NewsItem>) {
        // Criar um adaptador para o RecyclerView com a lista de notícias
        val adapterNews = NewsAdapter(requireContext(), newsList)
        recyclerViewNews.adapter = adapterNews

        // Verificar se a lista está vazia e exibir a mensagem apropriada
        if (newsList.isEmpty()) {
            recyclerViewNews.visibility = View.GONE
            emptyCardView.visibility = View.VISIBLE
            emptyTextView.text = "Without News..."

        } else {
            recyclerViewNews.visibility = View.VISIBLE
            emptyCardView.visibility = View.GONE
        }
    }

}




