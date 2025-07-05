package com.gmsoftva.videos.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PantallaAgregarAmigos() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Text("Pantalla de amigos", color = Color.White, fontSize = 24.sp)
    }
}


@Composable
fun PantallaPerfil(
    username: String,
    tipo: String, // "propio" o "ajeno"
    navController: NavController? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = if (tipo == "propio") "Perfil propio de $username" else "Perfil de $username",
                color = Color.White,
                fontSize = 24.sp
            )
            if (tipo == "ajeno" && navController != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { navController.popBackStack() }) {
                    Text("Volver")
                }
            }
        }
    }
}
