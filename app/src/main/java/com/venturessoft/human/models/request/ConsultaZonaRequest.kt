package com.venturessoft.human.models.request

data class ConsultaZonaRequest(
    var idUsuario: Long,
    var idZona: String? = null,
    var pagina: String? = null,
    var numRegistros: String? = null,
    var filtroZona: String? = null
)

