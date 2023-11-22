package com.example.projmobilegroupb

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MonitoredList {
    private const val PREFS_NAME = "MonitoredListPrefs"
    private const val KEY_ASSETS = "monitored_assets"

    var assets = mutableListOf<Asset>()
        private set

    // Adiciona um novo ativo à lista
    fun addAsset(asset: Asset, context: Context) {
        assets.add(asset)
        saveAssets(context)
    }

    // Remove um ativo específico
    fun removeAsset(asset: Asset, context: Context) {
        assets.remove(asset)
        saveAssets(context)
    }

    // Carrega os ativos monitorados salvos
    fun loadAssets(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_ASSETS, null)
        val type = object : TypeToken<MutableList<Asset>>() {}.type
        assets = Gson().fromJson(json, type) ?: mutableListOf()
    }

    // Salva os ativos monitorados
    private fun saveAssets(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(assets)
        editor.putString(KEY_ASSETS, json)
        editor.apply()
    }

    // Verifica se um ativo específico está na lista
    fun containsAsset(asset: Asset, context: Context): Boolean {
        return assets.contains(asset)
    }
}