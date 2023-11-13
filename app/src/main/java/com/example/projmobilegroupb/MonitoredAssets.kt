package com.example.projmobilegroupb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MonitoredAssets: Fragment(){

    private lateinit var textView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.monitored_assets_layout, container, false)

        val button = view.findViewById<Button>(R.id.image_toolbar)

        textView = view.findViewById(R.id.textView1)

        /*
        button.setOnClickListener {
            val nextFrag = FragmentTwo()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentHolder, nextFrag)
            transaction.addToBackStack(null)
            transaction.commit()
        }
         */

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadSummary()

    }

    private fun loadSummary(){
        val jsonApi = AssetClient.retrofit.create(JsonSummaryApi::class.java)

        jsonApi.getSummary(1).enqueue(object: Callback<Symbol> {

            override fun onResponse(call: Call<Symbol>, response: Response<Symbol>){
                if (response.isSuccessful){
                    //Atualizar o TextView com o simbolo da ação
                    textView.text = response.body()?.symbol ?: "Asset not found"
                } else {
                    textView.text = "Error: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<Symbol>, t: Throwable){
                //exebir a mensagem de erro
                textView.text = "Fail: ${t.message}"
            }
        })

    }
}