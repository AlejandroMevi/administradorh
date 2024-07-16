package com.venturessoft.human.models.request

data class BusquedaEmpleadoRequest(
    var idCia: Long,
    var filtroEmpleado: String? = null,
    var pagina: Int? = null,
    var numRegistros: Int? = null,
    var idUsuario: Long? = null
)

