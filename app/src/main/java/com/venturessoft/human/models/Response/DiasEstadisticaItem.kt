package com.venturessoft.human.models.response

data class DiasEstadisticaItem(
    var fecha: String,
    var totalEmpleados: Int,
    var totalEmpleadosChecados: Int,
    var porcentaje: Double
)

