package com.venturessoft.human.models.response

data class ReporteStationFreeRequest(
    var fechaInicio: String,
    var fechaFin: String? = null,
    var empls: List<Long>?=null,
    var numCia: String? = null,
    var idUsuario: String? = null
)