package com.example.projmobilegroupb

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projmobilegroupb.dataClasses.ChartData
import com.example.projmobilegroupb.dataClasses.Detail
import com.example.projmobilegroupb.retrofit.AssetClient
import com.example.projmobilegroupb.retrofit.JsonDetailsApi
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssetsDetail: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // seleciona a view
        val view = inflater.inflate(R.layout.symbol_details_layout, container, false)

        // botão para voltar á pagina da monitorização das ações
        val button = view.findViewById<TextView>(R.id.text_toolbar3)

        // a funcionalidade desse botão
        button.setOnClickListener {
            val nextFrag = MonitoredAssets()
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

        // lê qual a ação que foi selecionada para ver os detalhes
        val symbol = arguments?.getString("symbol") ?: null

        // seleciona o conteiner
        val assetContainer = view.findViewById<LinearLayout>(R.id.detailsContainer)

        if (assetContainer != null) {
            // seleciona a view dos detalhes
            val assetDetailView = layoutInflater.inflate(R.layout.assets_details_layout, null)

            // os textViews onde estarão cada um dos atributos apresentados
            val textView1 = assetDetailView.findViewById<TextView>(R.id.titleSymbol)
            val textView2 = assetDetailView.findViewById<TextView>(R.id.sector)
            val textView3 = assetDetailView.findViewById<TextView>(R.id.ceo)
            val textView4 = assetDetailView.findViewById<TextView>(R.id.description)
            val imageView = assetDetailView.findViewById<ImageView>(R.id.logoImageView)

            // selecionam um valor padrão
            textView1.text = symbol
            textView2.text = "Loading..."
            textView3.text = "Loading..."
            textView4.text = "Loading..."

            // adiciona uma view aos conteiners
            assetContainer.addView(assetDetailView)

            // carrega os detales não imagem da ação selecionada
            loadDetails(symbol, textView2, textView3, textView4)

            // carrega a imagem da ação selecionada
            loadLogo(symbol, imageView)

            val lineChart = view.findViewById<LineChart>(R.id.lineChart)
            loadChartData(symbol, lineChart)

        } else {
            // Handle the case where assetContainer is null
            // Log an error, show a message, or take appropriate action
        }
    }

    // carrega os detalhes não imagem da ação que está a ser consultada
    private fun loadDetails(symbol: String?, textView2: TextView, textView3: TextView, textView4: TextView) {
        // faz a busca dos dados á API
        val jsonApi = AssetClient.retrofit.create(JsonDetailsApi::class.java)

        if (symbol != null) {
            jsonApi.getDetails(symbol).enqueue(object : Callback<Detail> {

                override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                    if (response.isSuccessful) {
                        // muda o valor dos textos para os atributos correspondentes
                        textView2.text = "Sector: " + response.body()?.sector ?: "N/A"
                        textView3.text = "CEO: " + response.body()?.CEO ?: "N/A"
                        textView4.text = "Description: " + response.body()?.description ?: "N/A"

                    } else {
                        // caso haja erro, o erro é apresentado
                        textView2.text = "Error: ${response.code()}"
                        textView3.text = "Error"
                        textView4.text = "Error"
                    }
                }

                override fun onFailure(call: Call<Detail>, t: Throwable) {
                    // caso haja falha, a falha é apresentada
                    textView2.text = "Fail: ${t.message}"
                    textView3.text = "Fail"
                    textView4.text = "Fail"
                }
            })
        }
    }

    // carrega a imagem da ação que está a ser consultada
    private fun loadLogo(symbol: String?, logoImageView: ImageView) {
        // faz a busca dos dados á API
        val jsonApi = AssetClient.retrofit.create(JsonDetailsApi::class.java)

        // carrega a imagem usando o glide
        symbol?.let {
            jsonApi.getDetails(it).enqueue(object : Callback<Detail> {
                override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                    if (response.isSuccessful) {
                        val logoUrl = response.body()?.logo_url
                        logoUrl?.let {
                            // Use Glide to load the logo
                            Glide.with(requireContext())
                                .load(logoUrl)
                                .into(logoImageView)
                        }
                    } else {
                        // caso haja erro, o erro é apresentado
                        Log.d("AssetsDetail", "Error getting logo URL: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Detail>, t: Throwable) {
                    // caso haja falha, a falha é apresentada
                    Log.e("AssetsDetail", "Failed to get logo URL: ${t.message}")
                }
            })
        }
    }

    private fun loadChartData(symbol: String?, lineChart: LineChart) {
        val jsonApi = AssetClient.retrofit.create(JsonDetailsApi::class.java)

        symbol?.let {
            jsonApi.getDetails(it).enqueue(object : Callback<Detail> {
                override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                    if (response.isSuccessful) {
                        val chartData = response.body()?.chart_data
                        chartData?.let {
                            // Configure e exiba o gráfico aqui
                            setupLineChart(lineChart, it.October_2022)
                        }
                    } else {
                        // Trate os erros conforme necessário
                        Log.d("AssetsDetail", "Error getting chart data: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<Detail>, t: Throwable) {
                    // Trate as falhas conforme necessário
                    Log.e("AssetsDetail", "Failed to get chart data: ${t.message}")
                }
            })
        }
    }

    private fun setupLineChart(lineChart: LineChart, data: List<Double>) {
        val entries = ArrayList<Entry>()

        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), data[i].toFloat()))
        }

        val dataSet = LineDataSet(entries, "Chart Data")
        val lineData = LineData(dataSet)

        lineChart.data = lineData
        lineChart.invalidate() // Atualiza o gráfico
    }


}