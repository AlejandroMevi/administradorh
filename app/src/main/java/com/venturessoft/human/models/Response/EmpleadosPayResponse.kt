package com.venturessoft.human.models.response

data class EmpleadosPayResponse(
	val empleadosEstadistica: List<EmpleadosEstadisticaItem?>? = null,
	val codigo: String? = null
)

data class EmpleadosEstadisticaItem(
	val maximoEmpleados: Int? = null,
	val totalEmpleadosRegistrados: Int? = null,
	val porcentaje: Float? = null
)

