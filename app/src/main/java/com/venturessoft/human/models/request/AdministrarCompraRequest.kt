package com.venturessoft.human.models.request

data class AdministrarCompraRequest(
    var idAdmin: Long,
    var total: Double,
    var subtotal: Double,
    var fechaCompra: String,
    var folio: String,
    var numEmpleados: Int,
    var cvePais: String
)



