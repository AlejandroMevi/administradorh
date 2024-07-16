package com.venturessoft.human.models.request

data class ActualizaEstacionRequest(
    var idUsuario: Long,
    var idEstacion: Int,
    var idZona: Int? = null,
    var clvUnidad: String? = null,
    var uuid: String? = null,
    var bssid: String? = null,
    var latitud: Double? = null,
    var longitud: Double? = null,
    var rango: Double? = null,
    var nombre: String? = null,
    var status: String? = null,
    var estacionLibre: Boolean? = false
)


