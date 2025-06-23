package com.gmsoftva.videos

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun TikTokUI(showVideo: Boolean = true) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Fondo de video (solo cuando se ejecuta en app real)
        if (showVideo) {
            VideoPlayer(
                modifier = Modifier.fillMaxSize(),
                context = context,
                videoResId = R.raw.sample_video
            )
        } else {
            // Imagen de fondo estática para preview
            Image(
                painter = painterResource(id = R.drawable.preview_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Acciones del lado derecho
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileButton()
            Spacer(modifier = Modifier.height(24.dp))
            ActionIcon(R.drawable.ic_corazon, "1765")
            Spacer(modifier = Modifier.height(16.dp))
            ActionIcon(R.drawable.ic_comentarios, "732")
            Spacer(modifier = Modifier.height(16.dp))
            ActionIcon(R.drawable.ic_guardar, "321")
        }

        // Información del video en la parte inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            Text("@Chalito_ms", color = Color.White, fontSize = 16.sp)
            Text("#Hashtag", color = Color.White, fontSize = 14.sp)
            Text("🎵 Titulo - Titulo", color = Color.White, fontSize = 14.sp)
        }

        // Barra inferior de navegación
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .height(56.dp)
                .background(Color.Black),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavIcon(R.drawable.ic_inicio)
            BottomNavIcon(R.drawable.ic_amigos)
            BottomNavIcon(R.drawable.ic_agregar)
            BottomNavIcon(R.drawable.ic_mensajes)
            BottomNavIcon(R.drawable.ic_usuario)
        }
    }
}

@Composable
fun VideoPlayer(modifier: Modifier = Modifier, context: Context, videoResId: Int) {
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val uri = "android.resource://${context.packageName}/$videoResId"
            val mediaItem = MediaItem.fromUri(uri)
            setMediaItem(mediaItem)
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = {
            PlayerView(it).apply {
                this.player = player
                useController = false
            }
        }
    )
}

@Composable
fun ActionIcon(iconRes: Int, count: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        Text(count, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun BottomNavIcon(iconRes: Int) {
    Image(
        painter = painterResource(id = iconRes),
        contentDescription = null,
        modifier = Modifier.size(32.dp)
    )
}

@Composable
fun ProfileButton() {
    Image(
        painter = painterResource(id = R.drawable.ic_profile_circle),
        contentDescription = null,
        modifier = Modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(Color.White)
            .padding(4.dp)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, showBackground = true)
@Composable
fun TikTokUiPreview() {
    MaterialTheme {
        TikTokUI(showVideo = false) // Desactiva video para preview
    }
}