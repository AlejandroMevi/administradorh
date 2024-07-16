package com.venturessoft.human.models.request

data class ReporteEnrollRequest(
    var idCia: Long,
    var status: Boolean,
    var fechaInicio: String,
    var fechaFin: String,
    var idioma: String,
    var idUsuario: Long
)