package com.venturessoft.human.models.request

data class BusquedaUsuarioRequest(
    var idUsuario: Long,
    var filtroUsuario: String,
    var pagina: Int? = 1,
    var numRegistros: Long
)
