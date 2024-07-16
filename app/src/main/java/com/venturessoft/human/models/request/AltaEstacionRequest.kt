package com.venturessoft.human.models.request

data class AltaEstacionRequest(
    var idUsuario: Long,
    var idZona: Int,
    var clvUnidad: String,
    var uuid: String?,
    var bssid: String?,
    var latitud: Float,
    var longitud: Float,
    var rango: Float,
    var nombre: String,
    var estacionLibre: Boolean?
)
