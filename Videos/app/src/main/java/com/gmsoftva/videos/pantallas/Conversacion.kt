package com.gmsoftva.videos.pantallas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gmsoftva.videos.R
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun ConversacionScreen(
    username: String,
    navController: NavController
) {
    val mensajes = remember {
        mutableStateListOf(
            Mensaje("¡Hola! ¿Ya viste el video?", true),
            Mensaje("Sí", false),
            Mensaje("Me hizo reir", true),
            Mensaje("\uD83D\uDE02", false)
        )
    }

    var nuevoMensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_retornar),
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Aquí usamos el username recibido como argumento
            Text(
                text = username,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            reverseLayout = true
        ) {
            items(mensajes.reversed()) { mensaje ->
                BurbujaMensaje(mensaje)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray)
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 600.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nuevoMensaje,
                    onValueChange = { nuevoMensaje = it },
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(20.dp)),
                    placeholder = { Text("Escribe un mensaje...", color = Color.Gray) },
                    shape = RoundedCornerShape(20.dp),
                    maxLines = 6,
                    minLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1C1C1C),
                        unfocusedContainerColor = Color(0xFF1C1C1C),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.White
                    )
                )

                IconButton(onClick = {
                    if (nuevoMensaje.isNotBlank()) {
                        mensajes.add(Mensaje(nuevoMensaje.trim(), false))
                        nuevoMensaje = ""
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = "Enviar",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BurbujaMensaje(mensaje: Mensaje) {
    val bubbleColor = if (mensaje.esRemoto) Color.DarkGray else Color(0xFF1DB954)
    val alignment = if (mensaje.esRemoto) Arrangement.Start else Arrangement.End

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = alignment
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, shape = RoundedCornerShape(16.dp))
                .padding(12.dp)
                .widthIn(max = 370.dp)
        ) {
            Text(text = mensaje.texto, color = Color.White)
        }
    }
}

data class Mensaje(val texto: String, val esRemoto: Boolean)


data class MessageData(
    val profileImage: Int,
    val username: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0
)

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ConversacionScreenPreview() {
    // NavController simulado para preview (no navega realmente)
    val navController = rememberNavController()
    ConversacionScreen(username = "Gonzalo", navController = navController)
}
