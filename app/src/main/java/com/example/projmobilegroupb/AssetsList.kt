package com.example.projmobilegroupb

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.projmobilegroupb.dataClasses.Asset
import com.example.projmobilegroupb.retrofit.AssetClient
import com.example.projmobilegroupb.retrofit.JsonListApi
import com.example.projmobilegroupb.retrofit.MonitoredList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssetsList: Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // seleciona a view
        val view = inflater.inflate(R.layout.assets_list_layout, container, false)

        // botão para voltar á pagina da monitorização das ações
        val button = view.findViewById<TextView>(R.id.text_toolbar2)

        // a funcionalidade desse botão
        button.setOnClickListener {
            val nextFrag = MonitoredAssets()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // mostra a view
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // seleciona o conteiner
        val listContainer = view.findViewById<LinearLayout>(R.id.listContainer)

        // Passe o contexto (requireContext()) para loadList
        loadList(listContainer, requireContext())
    }

    // carrega a lista de ações
    private fun loadList(assetContainer: LinearLayout, context: Context) {
        // faz a busca dos dados á API
        val jsonListApi = AssetClient.retrofit.create(JsonListApi::class.java)

        jsonListApi.getList().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                if (response.isSuccessful) {
                    response.body()?.forEach { symbol ->
                        addSymbolToUI(symbol, assetContainer, context)
                    }
                } else {
                    // faz com que a app aguente com o erro
                }
            }

            override fun onFailure(call: Call<List<String>>, t: Throwable) {
                // faz com que a app aguente com o erro
            }
        })
    }

    // adiciona as ações e os seus interruptores á lista
    private fun addSymbolToUI(symbol: String, assetContainer: LinearLayout, context: Context) {
        val assetListView = layoutInflater.inflate(R.layout.symbols_list_layout, null)

        // seleciona o interruptor
        val switchSymbol = assetListView.findViewById<Switch>(R.id.switchSymbol)
        // dá o nome do interruptor
        switchSymbol.text = symbol

        // Verifica se a ação já está a ser monitorizada e atualiza o estado do interruptor
        switchSymbol.isChecked = MonitoredList.containsAsset(Asset("$symbol"), context)

        // adiciona ou remove a ação dependendo da mudança de estado do interruptor (on/off)
        switchSymbol.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Adicionando a ação
                MonitoredList.addAsset(Asset("$symbol"), context)
                Toast.makeText(context, "Symbol $symbol added to monitored assets", Toast.LENGTH_SHORT).show()
            } else {
                // Removendo a ação
                MonitoredList.removeAsset(Asset("$symbol"), context)
                Toast.makeText(context, "Symbol $symbol removed from monitored assets", Toast.LENGTH_SHORT).show()
            }
        }

        // adiciona o conteiner á view
        assetContainer.addView(assetListView)
    }
}
