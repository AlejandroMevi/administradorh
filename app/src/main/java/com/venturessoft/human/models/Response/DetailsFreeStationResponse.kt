package com.venturessoft.human.models.Response

data class DetailsFreeStationResponse(
	val codigo: String,
	val detalle: List<DetalleItem?>? = null
)

data class DetalleItem(
	val puesto: String? = null,
	val fechaChecada: String? = null,
	val latitud: String? = null,
	val tipo: String? = null,
	val longitud: String? = null,
	val horaChecada: String? = null,
	val numEmp: Int? = null,
	val numCia: Int? = null,
	val nombreEmp: String? = null,
	val fotografia: String? = null,
	val status: String? = null
)

