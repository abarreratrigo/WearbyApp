package com.example.wearbyandroid

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wearbyandroid.sesion.SesionUsuario
import com.example.wearbyandroid.ui.theme.login.LoginScreen
import com.example.wearbyandroid.ui.theme.registro.RegistroScreen

/**
 * Define las rutas de navegación de la aplicación.
 * Decide la pantalla inicial según si hay sesión activa.
 */
@Composable
fun NavegacionApp() {
    val navController = rememberNavController()
    val inicio = if (SesionUsuario.isLoggedIn()) "principal" else "login"

    NavHost(navController = navController, startDestination = inicio) {

        composable("login") {
            LoginScreen(
                onLoginExitoso = {
                    if (SesionUsuario.isAdmin()) {
                        navController.navigate("admin") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("principal") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onIrARegistro = { navController.navigate("registro") }
            )
        }

        composable("registro") {
            RegistroScreen(
                onRegistroExitoso = {
                    navController.navigate("principal") {
                        popUpTo("registro") { inclusive = true }
                    }
                },
                onIrALogin = { navController.popBackStack() }
            )
        }

        composable("principal") {
            // Se implementa en el siguiente paso
            androidx.compose.material3.Text("Pantalla principal - En construcción")
        }

        composable("admin") {
            // Se implementa en el siguiente paso
            androidx.compose.material3.Text("Panel admin - En construcción")
        }
    }
}