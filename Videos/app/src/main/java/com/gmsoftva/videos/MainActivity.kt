package com.gmsoftva.videos

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.gmsoftva.videos.ui.theme.TemaUI
import com.gmsoftva.videos.componentes.ActionIcon
import com.gmsoftva.videos.componentes.BottomNavIcon
import com.gmsoftva.videos.componentes.Comentarios
import com.gmsoftva.videos.componentes.Video
import com.gmsoftva.videos.pantallas.ConversacionScreen
import com.gmsoftva.videos.pantallas.PantallaAgregarAmigos
import com.gmsoftva.videos.pantallas.PantallaMensajes
import com.gmsoftva.videos.pantallas.PantallaPerfil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TemaUI {
                AppScreen()
            }
        }
    }
}

@Composable
fun AppScreen() {
    val navController = rememberNavController()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = Rutas.INICIO) {
            composable(Rutas.INICIO) {
                InicioUI(navController = navController)
            }
            composable(Rutas.AMIGOS) {
                PantallaAgregarAmigos()
            }
            composable(Rutas.MENSAJES) {
                PantallaMensajes(navController = navController)
            }
            composable(
                route = Rutas.PERFIL, // Ejemplo: "perfil/{username}/{tipo}"
                arguments = listOf(
                    navArgument("username") { type = NavType.StringType },
                    navArgument("tipo") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: ""
                val tipo = backStackEntry.arguments?.getString("tipo") ?: "ajeno"
                PantallaPerfil(username = username, tipo = tipo, navController = navController)
            }
            composable(
                route = Rutas.CONVERSACION,
                arguments = listOf(navArgument("username") { type = NavType.StringType })
            ) { backStackEntry ->
                val username = backStackEntry.arguments?.getString("username") ?: ""
                ConversacionScreen(
                    username = username,
                    navController = navController
                )
            }
        }

        BottomBar(
            navController = navController,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun BottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.Black),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavIcon(R.drawable.ic_inicio_a) {
            navController.navigate(Rutas.INICIO) {
                popUpTo(Rutas.INICIO) { inclusive = true }
            }
        }
        BottomNavIcon(R.drawable.ic_amigos_a) {
            navController.navigate(Rutas.AMIGOS) {
                popUpTo(Rutas.AMIGOS) { inclusive = true }
            }
        }
        BottomNavIcon(R.drawable.ic_agregar) { /* ... */ }
        BottomNavIcon(R.drawable.ic_mensajes_a) {
            navController.navigate(Rutas.MENSAJES) {
                popUpTo(Rutas.MENSAJES) { inclusive = true }
            }
        }
        BottomNavIcon(R.drawable.ic_usuario_a) {
            // Navegar al perfil propio, por ejemplo:
            navController.navigate(Rutas.perfil("miUsuario", "propio")) {
                popUpTo(Rutas.PERFIL) { inclusive = true }
            }
        }
    }
}

@Composable
fun RightSideBar(
    isLiked: Boolean,
    likeCount: Int,
    isSaved: Boolean,
    saveCount: Int,
    navController: NavController,
    onLikeClick: () -> Unit,
    onCommentsClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .offset(x = 300.dp, y = 300.dp)
            .padding(end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ActionIcon(
            iconRes = R.drawable.ic_circulo_perfil,
            onClick = {
                navController.navigate(Rutas.perfil("Chalito_ms", "ajeno")) {
                    launchSingleTop = true
                }
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        ActionIcon(
            iconRes = if (isLiked) R.drawable.ic_corazon_a else R.drawable.ic_corazon_c,
            count = likeCount.toString(),
            onClick = onLikeClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        ActionIcon(
            iconRes = R.drawable.ic_comentarios,
            count = "4",
            onClick = onCommentsClick
        )
        Spacer(modifier = Modifier.height(16.dp))
        ActionIcon(
            iconRes = if (isSaved) R.drawable.ic_guardar_a else R.drawable.ic_guardar_b,
            count = saveCount.toString(),
            onClick = onSaveClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioUI(navController: NavController, showVideo: Boolean = true) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableIntStateOf(1765) }
    var isSaved by remember { mutableStateOf(false) }
    var saveCount by remember { mutableIntStateOf(321) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSheet by remember { mutableStateOf(false) }

    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Color.DarkGray.copy(alpha = 0.95f)
        ) {
            Comentarios()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (showVideo) {
            Video(
                modifier = Modifier.fillMaxSize(),
                context = context,
                videoResId = R.raw.video
            )
        }
        /*
        else {

            Image(
                painter = painterResource(id = R.drawable.preview_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
         */

        RightSideBar(
            isLiked = isLiked,
            likeCount = likeCount,
            isSaved = isSaved,
            saveCount = saveCount,
            navController = navController,
            onLikeClick = {
                isLiked = !isLiked
                likeCount = if (isLiked) likeCount + 1 else likeCount - 1
            },
            onCommentsClick = { showSheet = true },
            onSaveClick = {
                isSaved = !isSaved
                saveCount = if (isSaved) saveCount + 1 else saveCount - 1
            }
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 16.dp, bottom = 80.dp)
        ) {
            Text("@Chalito_ms", color = Color.White, fontSize = 16.sp)
            Text("#Hashtag", color = Color.White, fontSize = 14.sp)
            Text("\uD83C\uDFB5 Titulo - Titulo", color = Color.White, fontSize = 14.sp)
        }
    }
}

// función fake para previews
@Composable
fun fakeNavController(): NavController {
    val context = LocalContext.current
    return remember {
        object : NavController(context) {}
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun TikTokUiPreview() {
    TemaUI {
        InicioUI(navController = fakeNavController(), showVideo = false)
    }
}

@Preview(showBackground = true)
@Composable
fun BottomBarPreview() {
    val navController = fakeNavController()
    TemaUI {
        BottomBar(navController = navController)
    }
}
