package com.venturessoft.human.models.request

data class BusquedaEstacionRequest(
    var idUsuario: Long,
    var filtroEstacion: String? = null
)
