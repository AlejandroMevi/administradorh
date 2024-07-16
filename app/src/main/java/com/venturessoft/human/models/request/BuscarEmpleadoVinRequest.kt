package com.venturessoft.human.models.request

data class BuscarEmpleadoVinRequest(
    var ciaNom: Int,
    var numEmp: Long,
    var fechaFoto: String,
    var idioma: String
)
