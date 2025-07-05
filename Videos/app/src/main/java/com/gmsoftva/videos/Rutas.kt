package com.gmsoftva.videos

object Rutas {
    const val INICIO = "inicio"
    const val AMIGOS = "amigos"
    const val MENSAJES = "mensajes"
    const val CONVERSACION = "conversacion/{username}"
    const val PERFIL = "perfil/{username}/{tipo}"


    // Funciones para crear rutas con argumentos
    fun conversacion(username: String) = "conversacion/$username"
    fun perfil(username: String, tipo: String) = "perfil/$username/$tipo"

}