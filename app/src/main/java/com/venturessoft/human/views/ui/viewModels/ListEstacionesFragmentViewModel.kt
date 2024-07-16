package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.*
import com.venturessoft.human.models.request.*
import com.venturessoft.human.models.response.*

class ListEstacionesFragmentViewModel {
    var busquedaEstacionResponseMutableData = MutableLiveData<BusquedaEstacionResponse>()
    var consultaEstacionUnoResponseMutableData = MutableLiveData<ConsultaEstacionResponse>()

    var busquedaZonaResponseMutableData = MutableLiveData<BusquedaZonaResponse>()


    var actualizaEstacionResponseMutableData = MutableLiveData<ActualizaEstacionResponse>()
    var eliminaEstacionResponseMutableData = MutableLiveData<EliminaEstacionResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }

    fun getBusquedaEstacion(busquedaEstacionRequest: BusquedaEstacionRequest) {
        isLoading.postValue(true)
        BusquedaEstacionRepository().getBusquedaEstacion(busquedaEstacionRequest, busquedaEstacionResponseMutableData, isLoading, codeError)
    }
    fun getConsultaEstacionUno(consultaEstacionRequest: ConsultaEstacionRequest) {
        isLoading.postValue(true)
        ConsultaEstacionRepository().getConsultaEstacion(consultaEstacionRequest, consultaEstacionUnoResponseMutableData, isLoading,codeError)
    }
    fun getActualizaEstacionService(actualizaEstacionRequest: ActualizaEstacionRequest){
        isLoading.postValue(true)
        ActualizaEstacionRepository().getActualizaEstacion(actualizaEstacionRequest,actualizaEstacionResponseMutableData,isLoading,codeError)
    }
    fun getEliminaEstacion(eliminaEstacionRequest: EliminaEstacionRequest) {
        isLoading.postValue(true)
        EliminaEstacionRepository().getEliminaEstacion(eliminaEstacionRequest, eliminaEstacionResponseMutableData, isLoading,codeError)
    }



    fun getBusquedaZonas(busquedaZonaRequest: BusquedaZonaRequest){
        isLoading.postValue(true)
        BusquedaZonaRepository().getBusquedaZonas(busquedaZonaRequest, busquedaZonaResponseMutableData,isLoading, codeError)
    }
}