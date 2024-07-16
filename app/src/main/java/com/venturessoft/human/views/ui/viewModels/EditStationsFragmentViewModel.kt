package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizaEstacionRepository
import com.venturessoft.human.repository.BusquedaZonaRepository
import com.venturessoft.human.repository.ConsultaEstacionRepository
import com.venturessoft.human.models.request.ActualizaEstacionRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.request.ConsultaEstacionRequest
import com.venturessoft.human.models.response.ActualizaEstacionResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaEstacionResponse

class EditStationsFragmentViewModel {
    var consultaEstacionResponseMutableData = MutableLiveData<ConsultaEstacionResponse>()
    //var consultaZonaResponseMutableData = MutableLiveData<ConsultaZonaResponse>()

    var busquedaZonaResponseMutableData = MutableLiveData<BusquedaZonaResponse>()

    var actualizaEstacionResponseMutableData = MutableLiveData<ActualizaEstacionResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }

    fun getConsultaEstacion(consultaEstacionRequest: ConsultaEstacionRequest) {
        isLoading.postValue(true)
        ConsultaEstacionRepository().getConsultaEstacion(consultaEstacionRequest, consultaEstacionResponseMutableData, isLoading,codeError)
    }

    fun getBusquedaZonas(busquedaZonaRequest: BusquedaZonaRequest){
        isLoading.postValue(true)
        BusquedaZonaRepository().getBusquedaZonas(busquedaZonaRequest,busquedaZonaResponseMutableData,isLoading,codeError)
    }

    fun getActualizaEstacionService(actualizaEstacionRequest: ActualizaEstacionRequest){
        isLoading.postValue(true)
        ActualizaEstacionRepository().getActualizaEstacion(actualizaEstacionRequest,actualizaEstacionResponseMutableData,isLoading,codeError)
    }
}