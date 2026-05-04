package com.example.wearbyandroid.modelo

data class Prenda(
    val id: Int,
    val nombre: String,
    val imagenUrl: String?,
    val favorito: Boolean,
    val categoria: Categoria?,
    val color: Color?,
    val estilo: Estilo?,
    val formalidad: Formalidad?,
    val temporada: Temporada?,
    val notas: String?
)
