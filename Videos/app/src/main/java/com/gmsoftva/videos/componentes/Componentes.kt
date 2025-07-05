package com.gmsoftva.videos.componentes

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput


@Composable
fun Video(
    modifier: Modifier = Modifier,
    context: Context,
    videoResId: Int
) {
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

    var showIcon by remember { mutableStateOf<String?>(null) }
    var isHoldingLeft by remember { mutableStateOf(false) }
    var isHoldingRight by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(player.isPlaying) }
    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    // ⏳ Actualizar posición del video en tiempo real
    LaunchedEffect(player) {
        while (true) {
            currentPosition = player.currentPosition
            duration = player.duration.takeIf { it > 0 } ?: 1L
            isPlaying = player.isPlaying
            kotlinx.coroutines.delay(200)
        }
    }

    // ⏪ Retroceso continuo al mantener presionado
    LaunchedEffect(isHoldingLeft) {
        while (isHoldingLeft) {
            player.seekTo((player.currentPosition - 1000).coerceAtLeast(0))
            showIcon = "⏪"
            kotlinx.coroutines.delay(300)
        }
    }

    // ⏩ Avance continuo al mantener presionado
    LaunchedEffect(isHoldingRight) {
        while (isHoldingRight) {
            player.seekTo((player.currentPosition + 1000).coerceAtMost(player.duration))
            showIcon = "⏩"
            kotlinx.coroutines.delay(300)
        }
    }

    // 🔄 Ocultar ícono después de 600ms
    LaunchedEffect(showIcon) {
        if (showIcon != null) {
            kotlinx.coroutines.delay(600)
            showIcon = null
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                PlayerView(it).apply {
                    useController = false
                    this.player = player
                }
            }
        )

        // 🎯 Zonas táctiles
        Row(Modifier.matchParentSize()) {
            // Retroceder (izquierda)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { isHoldingLeft = true },
                            onPress = {
                                isHoldingLeft = true
                                tryAwaitRelease()
                                isHoldingLeft = false
                            },
                            onTap = {
                                isPlaying = !isPlaying
                                if (isPlaying) {
                                    player.play()
                                    showIcon = "▶️"
                                } else {
                                    player.pause()
                                    showIcon = "⏸"
                                }
                            }
                        )
                    }
            )
            // Adelantar (derecha)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = { isHoldingRight = true },
                            onPress = {
                                isHoldingRight = true
                                tryAwaitRelease()
                                isHoldingRight = false
                            },
                            onTap = {
                                isPlaying = !isPlaying
                                if (isPlaying) {
                                    player.play()
                                    showIcon = "▶️"
                                } else {
                                    player.pause()
                                    showIcon = "⏸"
                                }
                            }
                        )
                    }
            )
        }

        // 🎥 Ícono visual (⏪ ⏩ ⏸ ▶️)
        showIcon?.let { icon ->
            Text(
                text = icon,
                fontSize = 64.sp,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // 📍 Controles inferiores: barra de progreso y botón play/pause
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 50.dp, start = 16.dp, end = 16.dp)
        ) {
            Slider(
                value = currentPosition.toFloat(),
                onValueChange = {
                    player.seekTo(it.toLong())
                    currentPosition = it.toLong()
                },
                valueRange = 0f..duration.toFloat(),
                modifier = Modifier.fillMaxWidth(),
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.Gray
                )
            )
        }
    }
}


@Composable
fun ActionIcon(iconRes: Int, count: String? = null, onClick: (() -> Unit)? = null) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
        // Solo muestra el texto si count no es nulo y no está vacío
        count?.takeIf { it.isNotEmpty() }?.let {
            Text(it, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun Comentarios() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
    ) {
        Text(
            text = "Comentarios (732)",
            color = Color.White,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        repeat(5) {
            Text(
                text = "\uD83D\uDC64 Usuario $it: Este es un comentario de ejemplo.",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}
@Composable
fun BottomNavIcon(iconResId: Int, onClick: () -> Unit) {
    Icon(
        painter = painterResource(id = iconResId),
        contentDescription = null,
        modifier = Modifier
            .size(28.dp)
            .clickable { onClick() },
        tint = Color.Unspecified
    )
}