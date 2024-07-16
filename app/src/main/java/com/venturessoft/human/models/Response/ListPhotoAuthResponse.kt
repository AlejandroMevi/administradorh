package com.venturessoft.human.models.Response

import java.io.Serializable

data class ListPhotoAuthResponse(
	val codigo: String? = null,
	val lstFotoLocal: List<LstFotoLocalItem?>? = null,
	val error: Boolean? = null
)

data class LstFotoLocalItem(
	var fotoActual: String? = null,
	var fotoNueva: String? = null,
	var idEmpleado: Int? = null,
	var idCia: Int? = null,
	var nombreEmpleado: String? = null
):Serializable

