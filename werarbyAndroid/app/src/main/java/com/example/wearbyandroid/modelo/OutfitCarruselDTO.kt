package com.example.wearbyandroid.modelo

data class OutfitCarruselDTO(
    val categoria: String,
    val categoriaId: Int,
    val prendas: List<PrendaFiltroDTO>
)
