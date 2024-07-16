package com.venturessoft.human.models

class ListAuditHistoryModel {
    var id: String? = null
    var numCia: Long? = 0
    var numEmp: Long? = 0
    var fechaChecada: String? = ""
    var tipo: String? = ""
    var codigoEtime: String? = ""
    fun getIds(): String {
        return id!!.toString()
    }

    fun setIds(id: String) {
        this.id = id
    }

    fun getNumCia(): Long {
        return numCia!!.toLong()
    }

    fun setNumCia(numCia: Long) {
        this.numCia = numCia
    }

    fun getNumEmp(): Long {
        return numEmp!!.toLong()
    }

    fun setNumEmp(numEmp: Long) {
        this.numEmp = numEmp
    }

    fun getFechaChecadaa(): String {
        return fechaChecada!!.toString()
    }

    fun setFechaChecadaa(fechaChecada: String) {
        this.fechaChecada = fechaChecada
    }

    fun getTipoo(): String {
        return tipo!!.toString()
    }

    fun setTipoo(tipo: String) {
        this.tipo = tipo
    }

    fun getCodigoEtimee(): String {
        return codigoEtime!!.toString()
    }

    fun setCodigoEtimee(codigoEtime: String) {
        this.codigoEtime = codigoEtime
    }
}