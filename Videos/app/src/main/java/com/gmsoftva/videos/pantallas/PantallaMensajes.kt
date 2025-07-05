package com.gmsoftva.videos.pantallas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gmsoftva.videos.R
import androidx.navigation.NavController
import com.gmsoftva.videos.Rutas
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

@Composable
fun PantallaMensajes(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Text(
            text = "Bandeja de entrada",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp)
        ) {
            items(getDummyMessages()) { message ->
                MensajeItem(
                    message = message,
                    onClick = {
                        // Navegar a la conversación usando la ruta con argumento
                        navController.navigate(Rutas.conversacion(message.username))
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.Gray.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
private fun MensajeItem(
    message: MessageData,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = message.profileImage),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = message.username,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = message.lastMessage,
                    color = Color.Gray,
                    fontSize = 13.sp
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = message.time,
                    color = Color.Gray,
                    fontSize = 11.sp
                )
                if (message.unreadCount > 0) {
                    Badge(
                        containerColor = Color.Red,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Text(
                            text = message.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

private fun getDummyMessages(): List<MessageData> = listOf(
    MessageData(R.drawable.ic_circulo_perfil, "Nuevos seguidores", "Tommyboy empezó a seguirte", "", 3),
    MessageData(R.drawable.ic_circulo_perfil, "Actividad", "Emily comentó tu video", "10:30 AM", 1),
    MessageData(R.drawable.ic_circulo_perfil, "TikTok Shop", "Tu pedido ha sido enviado", "Ayer"),
    MessageData(R.drawable.ic_circulo_perfil, "Notificaciones", "Nuevas actualizaciones disponibles", "Lunes"),
    MessageData(R.drawable.ic_circulo_perfil, "pili❤", "Compartiste un video", "06/24/2025", 1),
    MessageData(R.drawable.ic_circulo_perfil, "mel22her", "Compartiste un video", "06/21/2025", 1),
    MessageData(R.drawable.ic_circulo_perfil, "Laura", "Compartió un video", "05/18/25", 1)
)

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun PantallaMensajesPreview() {
    val navController = rememberNavController()
    PantallaMensajes(navController = navController)
}
