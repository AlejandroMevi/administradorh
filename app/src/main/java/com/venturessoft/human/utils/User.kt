package com.venturessoft.human.utils

import android.app.Application
import com.venturessoft.human.models.response.sCia
import com.venturessoft.human.models.response.sZonas

class User : Application() {
    companion object {
        var email: String = ""
        var codigo: String = ""
        var ambiente: String = ""
        var idUsuario: Long = 0
        var estaciones: Boolean? = false
        var nombre: String = ""
        var nombreCompleto: String = ""
        var apellidoPat: String = ""
        var apellidoMat: String = ""
        var codigoTel: String = ""
        var telefono: String = ""
        var tipoUsuario: Int = 0
        var claveCia: String = ""
        var scia: ArrayList<sCia>? = null
        var szona: ArrayList<sZonas>? = null
        var token: String = ""
        var tokenDevice: String = ""
        var supertoken: String = ""
        var fotografia: String = ""
        var pass: String = ""
        var idCompani: Long? = 0
        var fotoLocal: String = ""
        var urlAvisoPriv: String = ""

        //se agrega estatus
        //var estatus:String=""
        //Vinculado
        var ciaVinculado: Long = 0
        var ciaSeleccionada: Long? = null
        var idZona: Int = 0
        var freeTrial: Boolean = false
        var expiracion: Boolean = false
        var fechaVigencia: String = ""
        var fechaActual: String = ""
        var idUsuFree: String = ""
        var costoEmpleado: Double = 0.0
        var maximoEmpleados: Int = 0
    }
}