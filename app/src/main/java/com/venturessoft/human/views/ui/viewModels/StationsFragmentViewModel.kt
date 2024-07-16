package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.AltaEstacionRepository
import com.venturessoft.human.repository.BusquedaZonaRepository
import com.venturessoft.human.models.request.AltaEstacionRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.response.AltaEstacionResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaZonaResponse

class StationsFragmentViewModel {
    var altaEstacionResponseMutableData = MutableLiveData<AltaEstacionResponse>()
    var consultaZonaResponseMutableData = MutableLiveData<ConsultaZonaResponse>()

    var busquedaZonaResponseMutableData = MutableLiveData<BusquedaZonaResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }



    fun getAltaEstacion(altaEstacionRequest: AltaEstacionRequest){
        isLoading.postValue(true)
        AltaEstacionRepository().getAltaEstaciones(altaEstacionRequest,altaEstacionResponseMutableData,isLoading,codeError)
    }

    fun getBusquedaZonas(busquedaZonaRequest: BusquedaZonaRequest){
        isLoading.postValue(true)
        BusquedaZonaRepository().getBusquedaZonas(busquedaZonaRequest,busquedaZonaResponseMutableData,isLoading,codeError)
    }
}