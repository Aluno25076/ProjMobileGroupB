package com.example.projmobilegroupb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var frag = MonitoredAssets()

        supportFragmentManager.beginTransaction().add(R.id.fragmentHolder, frag).commit()
    }
}