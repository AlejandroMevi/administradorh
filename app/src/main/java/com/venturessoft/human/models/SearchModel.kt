package com.venturessoft.human.models

class SearchModel {
    var id: String? = null
    var name: String? = null
    var number: String? = null
    var numerZone: String? = null
    var status: String? = null
    var apPaterno: String? = null
    var apMaterno: String? = null
    var horaEntrada: String? = ""
    var nameStation: String? = ""
    var fecha: String? = null
    var horaSalida: String? = ""
    var correo: String? = null
    var station: Boolean? = false
    var foto: String? = null

    // Se agrega
    var codigo: String? = ""
    var idZona: Int? = 0
    var totalRegistros: Int? = 0
    private val image_drawable: Int = 0

    fun getNames(): String {
        return name.toString()
    }

    fun setNames(name: String) {
        this.name = name
    }

    fun getNumberr(): String {
        return number.toString()
    }

    fun setNumberr(number: String) {
        this.number = number
    }

    fun getApPaternoo(): String {
        return apPaterno.toString()
    }

    fun setApPaternoo(apPaterno: String) {
        this.apPaterno = apPaterno
    }

    fun getApMaternoo(): String {
        return apMaterno.toString()
    }

    fun setApMaternoo(apMaterno: String) {
        this.apMaterno = apMaterno
    }

    fun getStatuss(): String {
        return status.toString()
    }

    fun setStatuss(status: String) {
        this.status = status
    }

    fun setIdd(id: String) {
        this.id = id
    }

    fun getIdd(): String {
        return id.toString()
    }

    fun getCorreoo(): String {
        return correo.toString()
    }

    fun setCorreoo(correo: String) {
        this.correo = correo
    }

    fun setHoraEntradaa(horaEntrada: String) {
        this.horaEntrada = horaEntrada
    }

    fun getHoraEntradaa(): String {
        return horaEntrada.toString()
    }

    fun setHoraSalidaa(horaSalida: String) {
        this.horaSalida = horaSalida
    }

    fun getHoraSalidaa(): String {
        return horaSalida.toString()
    }

    fun getFotoo(): String {
        return foto.toString()
    }

    fun setFotoo(foto: String) {
        this.foto = foto
    }

    fun setFechaa(fecha: String) {
        this.fecha = fecha
    }

    fun getFechaa(): String {
        return fecha.toString()
    }

    fun setNameStationn(nameStation: String) {
        this.nameStation = nameStation
    }

    fun getNameStationn(): String {
        return nameStation.toString()
    }

    fun getStationn(): Boolean {
        return station!!
    }

    fun setStationn(station: Boolean) {
        this.station = station
    }

    fun getIdZona(): Int {
        return idZona!!
    }

    fun setIdZona(idZona: Int) {
        this.idZona = idZona
    }
}