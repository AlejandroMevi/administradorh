package com.venturessoft.human.models.request

data class ReporteOpcionRequest(
    var idUsuario: Long,
    var numeroEmpleado: Int? = null,
    var idCia: Long,
    var idCiaSupervisor: Long,
    var fechaInicio: String,
    var fechaFin: String,
    var opcion: String,
    var idioma: String
)

