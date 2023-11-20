package com.example.projmobilegroupb

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssetsList: Fragment(){
    companion object {
        const val PREFS_NAME = "MyPrefs"
    }

    private val sharedPreferencesName = "selectedSymbols"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.assets_list_layout, container, false)

        val button = view.findViewById<TextView>(R.id.text_toolbar2)

        button.setOnClickListener {
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

        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val listContainer = view.findViewById<LinearLayout>(R.id.listContainer2)

        loadList(listContainer)

    }

    private fun loadList(assetContainer: LinearLayout) {
        val jsonListApi = AssetClient.retrofit.create(JsonListApi::class.java)

        jsonListApi.getList().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach { symbol ->
                        addSymbolToUI(symbol, assetContainer)
                    }
                } else {
                    // Handle unsuccessful response
                    // ...
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // Handle failure
                // ...
            }
        })
    }

    /*
    private fun addSymbolToUI(symbol: String, assetContainer: LinearLayout) {
        val assetListView = layoutInflater.inflate(R.layout.symbols_list_layout, null)

        val textViewSymbol = assetListView.findViewById<TextView>(R.id.textViewSymbol)
        textViewSymbol.text = symbol

        assetContainer.addView(assetListView)
    }
    */
    private fun addSymbolToUI(symbol: String, assetContainer: LinearLayout) {
        val assetListView = layoutInflater.inflate(R.layout.symbols_list_layout, null)

        val textViewSymbol = assetListView.findViewById<TextView>(R.id.textViewSymbol)
        textViewSymbol.text = symbol

        val sharedPreferences = requireActivity().getPreferences(Context.MODE_PRIVATE)
        val selectedSymbolsSet = sharedPreferences.getStringSet(PREFS_NAME, mutableSetOf()) ?: mutableSetOf()

        if (selectedSymbolsSet.contains(symbol)) {
            textViewSymbol.setTextColor(Color.GRAY)
        }

        /*
        textViewSymbol.setOnClickListener {
            selectedSymbolsSet.add(symbol)
            sharedPreferences.edit().putStringSet("selectedSymbols", selectedSymbolsSet).apply()

            textViewSymbol.setTextColor(Color.GRAY)
        }
        */

        textViewSymbol.setOnClickListener {
            val selectedSymbolsSet = sharedPreferences.getStringSet(PREFS_NAME, mutableSetOf()) ?: mutableSetOf()
            selectedSymbolsSet.add(symbol)
            sharedPreferences.edit().putStringSet(PREFS_NAME, selectedSymbolsSet).apply()
            textViewSymbol.setTextColor(Color.GRAY)
        }

        assetContainer.addView(assetListView)
    }

}
