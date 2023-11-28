package com.example.projmobilegroupb


import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.projmobilegroupb.dataClasses.Symbol
import com.example.projmobilegroupb.retrofit.AssetClient
import com.example.projmobilegroupb.retrofit.JsonSummaryApi
import com.example.projmobilegroupb.retrofit.MonitoredList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MonitoredAssets: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // seleciona a view
        val view = inflater.inflate(R.layout.monitored_assets_layout, container, false)

        // botão para ir á pagina da lista de ações
        val button = view.findViewById<ImageView>(R.id.image_toolbar)

        // a funcionalidade desse botão
        button.setOnClickListener {
            val nextFrag = AssetsList()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //botão para ír á pagina das noticias
        val buttonNews = view.findViewById<TextView>(R.id.text_toolbar)

        //a funcionalidade desse botão
        buttonNews.setOnClickListener {
            val nextFrag = NewsAssets()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder,nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // mostra a view
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // lê quais as ações estão a ser monitorizadas
        val symbols = MonitoredList.assets ?: emptyList()

        // seleciona o conteiner
        val assetContainer = view.findViewById<LinearLayout>(R.id.assetContainer)

        // itera cada simbolo das ações que estão
        symbols.forEach { asset ->
            // seleciona a view do summario
            val assetSummaryView = layoutInflater.inflate(R.layout.asset_summary_layout, null)

            // os textViews onde estarão cada um dos atributos apresentados
            val textView1 = assetSummaryView.findViewById<TextView>(R.id.textView1)
            val textView2 = assetSummaryView.findViewById<TextView>(R.id.textView2)
            val textView3 = assetSummaryView.findViewById<TextView>(R.id.textView3)

            // selecionam um valor padrão
            textView1.text = asset.asset
            textView2.text = "Loading..."
            textView3.text = "Loading..."

            // Faz os textos azuis e sublinhados
            textView1.setTextColor(resources.getColor(android.R.color.holo_blue_dark))
            textView1.paintFlags = textView1.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            // faz esta funcionalidade quando clica no simbolo da ação
            textView1.setOnClickListener {
                onSymbolClicked(asset.asset)
            }

            // adiciona uma view aos conteiners
            assetContainer.addView(assetSummaryView)

            // carrega o somario das ações
            loadSummary(asset.asset, textView2, textView3)
        }
    }

    // vai para a pagina dos detalhes do simbolo selecionado
    private fun onSymbolClicked(symbol: String) {
        val nextFrag = AssetsDetail()
        val bundle = Bundle()
        bundle.putString("symbol", symbol)
        nextFrag.arguments = bundle

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentHolder, nextFrag)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // carrega o summario das ações que estão a ser monitorizadas
    private fun loadSummary(symbol: String, textView2: TextView, textView3: TextView) {
        val jsonApi = AssetClient.retrofit.create(JsonSummaryApi::class.java)

        // faz a busca dos dados á API
        jsonApi.getSummary(symbol).enqueue(object : Callback<Symbol> {

            override fun onResponse(call: Call<Symbol>, response: Response<Symbol>) {
                if (response.isSuccessful) {
                    val changePercent = response.body()?.change_percent?.toDoubleOrNull() ?: 0.0

                    // muda o valor dos textos para os atributos correspondentes
                    textView2.text = response.body()?.current_price ?: "N/A"
                    textView3.text = response.body()?.change_percent ?: "N/A"

                    // Alterar a cor do texto com base no percentual de alteração
                    if (changePercent > 0) {
                        // Se o percentual de alteração for positivo, defina a cor verde
                        textView3.setTextColor(resources.getColor(R.color.green))
                    } else if (changePercent < 0) {
                        // Se o percentual de alteração for negativo, defina a cor vermelha
                        textView3.setTextColor(resources.getColor(R.color.red))
                    } else {
                        // Se o percentual de alteração for zero, defina a cor padrão
                        textView3.setTextColor(resources.getColor(android.R.color.black))
                    }
                } else {
                    // caso haja erro, o erro é apresentado
                    textView2.text = "Error: ${response.code()}"
                    textView3.text = "Error"
                }
            }

            override fun onFailure(call: Call<Symbol>, t: Throwable) {
                // caso haja falha, a falha é apresentada
                textView2.text = "Fail: ${t.message}"
                textView3.text = "Fail"
            }
        })
    }
}