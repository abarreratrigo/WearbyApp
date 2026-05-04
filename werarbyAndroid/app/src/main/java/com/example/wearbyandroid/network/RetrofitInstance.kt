package com.example.wearbyandroid.network

/**
 * Instancia singleton del servicio de API.
 * Evita crear múltiples instancias de Retrofit.
 */
object RetrofitInstance {
    val api: ApiService by lazy {
        ApiClient.retrofit.create(ApiService::class.java)
    }
}