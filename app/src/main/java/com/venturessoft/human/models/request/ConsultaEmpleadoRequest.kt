package com.venturessoft.human.models.request

data class ConsultaEmpleadoRequest(
    var idCia: Long,
    var idEmpleado: Long? = null,
    var pagina: Int? = null,
    var numRegistros: Int? = null,
    var idUsuario: Long
)

