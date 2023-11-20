package com.example.projmobilegroupb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonitoredAssets: Fragment(){

    private val sharedPreferencesKey = "selectedSymbols"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.monitored_assets_layout, container, false)

        val button = view.findViewById<ImageView>(R.id.image_toolbar)

        button.setOnClickListener {
            val nextFrag = AssetsList()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        /*Button News - Next Fragment - Inicio*/

        val buttonNews = view.findViewById<TextView>(R.id.text_toolbar)

        buttonNews.setOnClickListener {
            val nextFrag = NewsAssets()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder,nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        /*Button News - Next Fragment - Fim*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = activity?.getPreferences(Context.MODE_PRIVATE)
        val savedSymbols = sharedPreferences?.getStringSet(sharedPreferencesKey, setOf())?.toList()

        val symbols = savedSymbols ?: listOf("FB", "AAPL", "GOOGL")

        val assetContainer = view.findViewById<LinearLayout>(R.id.assetContainer)

        symbols.forEach { symbol ->
            val assetSummaryView = layoutInflater.inflate(R.layout.asset_summary_layout, null)

            val textView1 = assetSummaryView.findViewById<TextView>(R.id.textView1)
            val textView2 = assetSummaryView.findViewById<TextView>(R.id.textView2)
            val textView3 = assetSummaryView.findViewById<TextView>(R.id.textView3)

            textView1.text = symbol
            textView2.text = "Loading..."
            textView3.text = "Loading..."

            assetContainer.addView(assetSummaryView)

            loadSummary(symbol, textView2, textView3)
        }
    }


    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val symbols = listOf("FB", "AAPL", "GOOGL")

        val assetContainer = view.findViewById<LinearLayout>(R.id.assetContainer)

        symbols.forEach { symbol ->
            val assetSummaryView = layoutInflater.inflate(R.layout.asset_summary_layout, null)

            val textView1 = assetSummaryView.findViewById<TextView>(R.id.textView1)
            val textView2 = assetSummaryView.findViewById<TextView>(R.id.textView2)
            val textView3 = assetSummaryView.findViewById<TextView>(R.id.textView3)

            textView1.text = symbol
            textView2.text = "Loading..."
            textView3.text = "Loading..."

            assetContainer.addView(assetSummaryView)

            loadSummary(symbol, textView2, textView3)
        }
    }
     */

    private fun loadSummary(symbol: String, textView2: TextView, textView3: TextView) {
        val jsonApi = AssetClient.retrofit.create(JsonSummaryApi::class.java)

        jsonApi.getSummary(symbol).enqueue(object : Callback<Symbol> {

            override fun onResponse(call: Call<Symbol>, response: Response<Symbol>) {
                if (response.isSuccessful) {
                    // Update the TextViews with the fetched data
                    textView2.text = response.body()?.current_price ?: "N/A"
                    textView3.text = response.body()?.change_percent ?: "N/A"
                } else {
                    // Handle unsuccessful response
                    textView2.text = "Error: ${response.code()}"
                    textView3.text = "Error"
                }
            }

            override fun onFailure(call: Call<Symbol>, t: Throwable) {
                // Handle failure
                textView2.text = "Fail: ${t.message}"
                textView3.text = "Fail"
            }
        })
    }
}