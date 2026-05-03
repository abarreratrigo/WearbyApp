package com.example.wearbyandroid.modelo

data class Usuario(
    val id: Int,
    val nombre: String,
    val email: String,
    val rol: String,
    val activo: Boolean
)
