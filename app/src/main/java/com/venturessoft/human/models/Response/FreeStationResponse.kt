package com.venturessoft.human.models.Response

data class FreeStationResponse(
	val codigo: String,
	val estacionesEmp: List<EstacionesEmpItem?>? = null
)

data class EstacionesEmpItem(
	var numEmp: Int? = null,
	var estacion: String? = null,
	var id: String? = null,
	var idEstacion: String? = null,
	var numCia: Int? = null,
	var nombreEmp: String? = null,
	var fechaChecada: String? = null,
	var horaChecada: String? = null,
	var tipo: String? = null,
)

