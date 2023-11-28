package com.example.projmobilegroupb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.projmobilegroupb.retrofit.MonitoredList

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // carrega o contexto da aplicação (Shared Preferences)
        MonitoredList.loadAssets(applicationContext)

        // seleciona o fragmento
        var frag = MonitoredAssets()

        // inicia a aplicação no fragmento MonitoredAssets
        supportFragmentManager.beginTransaction().add(R.id.fragmentHolder, frag).commit()
    }
}