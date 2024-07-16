package com.venturessoft.human.models.response

class EstacionesEmpResponse {
    var foto: String? = null
    var id: String? = null
    var idEstacion: String? = null
    var nombreEmp: String? = null
    var numCia: Int? = null
    var numEmp: Int? = null
    var razonSocial: String? = null

    //se agrega
    var codigo: String? = null
    var estacionLibreDetalle: ArrayList<EstacionLibreDetalleResponse>? = null
}