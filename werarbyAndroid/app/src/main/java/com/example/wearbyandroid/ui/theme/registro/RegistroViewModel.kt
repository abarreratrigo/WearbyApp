package com.example.wearbyandroid.ui.theme.registro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearbyandroid.network.RetrofitInstance
import com.example.wearbyandroid.sesion.SesionUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class RegistroEstado(
    val nombre: String = "",
    val email: String = "",
    val contrasena: String = "",
    val confirmar: String = "",
    val cargando: Boolean = false,
    val error: String = ""
)

/**
 * ViewModel de la pantalla de registro.
 * Valida los datos y llama a la API REST para crear la cuenta.
 */
class RegistroViewModel : ViewModel() {

    private val _estado = MutableStateFlow(RegistroEstado())
    val estado: StateFlow<RegistroEstado> = _estado

    fun onNombreChange(v: String) { _estado.value = _estado.value.copy(nombre = v, error = "") }
    fun onEmailChange(v: String) { _estado.value = _estado.value.copy(email = v, error = "") }
    fun onContrasenaChange(v: String) { _estado.value = _estado.value.copy(contrasena = v, error = "") }
    fun onConfirmarChange(v: String) { _estado.value = _estado.value.copy(confirmar = v, error = "") }

    fun onRegistro(onExito: () -> Unit) {
        val estado = _estado.value

        if (estado.nombre.isEmpty() || estado.email.isEmpty() || estado.contrasena.isEmpty()) {
            _estado.value = _estado.value.copy(error = "Rellena todos los campos.")
            return
        }
        if (estado.contrasena != estado.confirmar) {
            _estado.value = _estado.value.copy(error = "Las contraseñas no coinciden.")
            return
        }
        if (estado.contrasena.length < 6) {
            _estado.value = _estado.value.copy(error = "La contraseña debe tener al menos 6 caracteres.")
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, error = "")
            try {
                val respuesta = RetrofitInstance.api.registrar(
                    mapOf(
                        "nombre" to estado.nombre,
                        "email" to estado.email,
                        "contrasena" to estado.contrasena
                    )
                )
                if (respuesta.isSuccessful && respuesta.body() != null) {
                    SesionUsuario.iniciar(respuesta.body()!!)
                    onExito()
                } else {
                    _estado.value = _estado.value.copy(
                        error = "El email ya está registrado.",
                        cargando = false
                    )
                }
            } catch (e: Exception) {
                _estado.value = _estado.value.copy(
                    error = "No se puede conectar con el servidor.",
                    cargando = false
                )
            }
        }
    }
}