package com.example.wearbyandroid.sesion

import com.example.wearbyandroid.modelo.Usuario

/**
 * Almacena los datos del usuario autenticado durante la sesión.
 * Patrón Singleton: una única instancia accesible globalmente.
 */
object SesionUsuario {

    private var usuario: Usuario? = null

    fun iniciar(u: Usuario) {
        usuario = u
    }

    fun cerrar() {
        usuario = null
    }

    fun getId(): Int = usuario?.id ?: 0
    fun getNombre(): String = usuario?.nombre ?: ""
    fun getEmail(): String = usuario?.email ?: ""
    fun getRol(): String = usuario?.rol ?: ""
    fun isAdmin(): Boolean = usuario?.rol == "admin"
    fun isLoggedIn(): Boolean = usuario != null
}