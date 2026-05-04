package com.example.wearbyandroid.ui.theme.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearbyandroid.network.RetrofitInstance
import com.example.wearbyandroid.sesion.SesionUsuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginEstado(
    val email: String = "",
    val contrasena: String = "",
    val cargando: Boolean = false,
    val error: String = ""
)

/**
 * ViewModel de la pantalla de login.
 * Gestiona el estado y la llamada a la API REST
 * de forma asíncrona mediante corrutinas.
 */
class LoginViewModel : ViewModel() {

    private val _estado = MutableStateFlow(LoginEstado())
    val estado: StateFlow<LoginEstado> = _estado

    fun onEmailChange(email: String) {
        _estado.value = _estado.value.copy(email = email, error = "")
    }

    fun onContrasenaChange(contrasena: String) {
        _estado.value = _estado.value.copy(contrasena = contrasena, error = "")
    }

    fun onLogin(onExito: () -> Unit) {
        val email = _estado.value.email.trim()
        val contrasena = _estado.value.contrasena

        if (email.isEmpty() || contrasena.isEmpty()) {
            _estado.value = _estado.value.copy(
                error = "Por favor, rellena todos los campos."
            )
            return
        }

        viewModelScope.launch {
            _estado.value = _estado.value.copy(cargando = true, error = "")
            try {
                val respuesta = RetrofitInstance.api.login(
                    mapOf("email" to email, "contrasena" to contrasena)
                )
                if (respuesta.isSuccessful && (respuesta.body() != null)) {
                    SesionUsuario.iniciar(respuesta.body()!!)
                    onExito()
                } else {
                    _estado.value = _estado.value.copy(
                        error = "Email o contraseña incorrectos.",
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