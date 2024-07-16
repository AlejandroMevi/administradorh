package com.venturessoft.human.models.request

data class ReporteChecadasRequest(
    var fechaInicio: String,
    var fechaFin: String? = null,
    var numeroEmpleado: Long? = null,
    var idCia: Long? = null,
    var idUsuario: Long? = null
)


