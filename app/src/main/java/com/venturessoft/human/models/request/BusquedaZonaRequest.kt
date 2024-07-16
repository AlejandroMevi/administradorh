package com.venturessoft.human.models.request

data class BusquedaZonaRequest(
    var idUsuario: Long,
    var idZona: String? = null,
    var pagina: String = "1",
    var numRegistros: String = "9000",
    var filtroZona: String = ""
)

