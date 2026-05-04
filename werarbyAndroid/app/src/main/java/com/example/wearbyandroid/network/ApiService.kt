package com.example.wearbyandroid.network

import com.example.wearbyandroid.modelo.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

/**
 * Define los endpoints de la API REST
 * Retrofit implementa automáticamente esta interfaz.
 */
interface ApiService {

    @POST("usuarios/login")
    suspend fun login(@Body credenciales: Map<String, String>): Response<Usuario>

    @POST("usuarios/registro")
    suspend fun registrar(@Body datos: Map<String, String>): Response<Usuario>

    @PUT("usuarios({id}")
    suspend fun editarPerfil(
        @Path("id") id: Int,
        @Body datos: Map<String, String>
    ): Response<Usuario>

    @GET("prendas/usuario/{usuarioId}")
    suspend fun getPrendas(@Path("usuarioId") usuarioId: Int): Response<List<Prenda>>

    @Multipart
    @POST("prendas")
    suspend fun anadirPrenda(
        @Part("usuarioId") usuarioId: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("categoriaId") categoriaId: RequestBody,
        @Part("colorId") colorId: RequestBody,
        @Part("estiloId") estiloId: RequestBody,
        @Part("formalidadId") formalidadId: RequestBody,
        @Part("temporadaId") temporadaId: RequestBody,
        @Part("notas") notas: RequestBody,
        @Part imagen: MultipartBody.Part?
    ): Response<Prenda>

    @PUT("prendas/{id}/favorito")
    suspend fun toggleFavorito(@Path("id") id: Int): Response<Prenda>

    @DELETE("prendas/{id}")
    suspend fun eliminarPrenda(@Path("id") id: Int): Response<Void>

    // Características
    @GET("caracteristicas/categorias")
    suspend fun getCategorias(): Response<List<Categoria>>

    @GET("caracteristicas/colores")
    suspend fun getColores(): Response<List<Color>>

    @GET("caracteristicas/estilos")
    suspend fun getEstilos(): Response<List<Estilo>>

    @GET("caracteristicas/formalidades")
    suspend fun getFormalidades(): Response<List<Formalidad>>

    @GET("caracteristicas/temporadas")
    suspend fun getTemporadas(): Response<List<Temporada>>

    // Outfits
    @POST("outfits/generar")
    suspend fun generarOutfit(@Body solicitud: Map<String, Any>): Response<List<OutfitCarruselDTO>>
}