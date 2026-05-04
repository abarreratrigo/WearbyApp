package com.example.wearbyandroid.modelo

data class PrendaFiltroDTO(
    val id: Int,
    val nombre: String,
    val imagenUrl: String?,
    val favorito: Boolean,
    val categoria: String?,
    val color: String?,
    val estilo: String?,
    val temporada: String?,
    val formalidad: String?
)
