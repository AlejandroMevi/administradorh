package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.venturessoft.human.repository.*
import com.venturessoft.human.models.request.*
import com.venturessoft.human.models.response.*

class ListZonesFragmentViewModel: ViewModel()  {
    var busquedaZonaResponseMutableData = MutableLiveData<BusquedaZonaResponse>()
    var altaZonaResponseMutableData = MutableLiveData<AltaZonasResponse>()
    var eliminaZonaResponseMutableData = MutableLiveData<EliminaZonaResponse>()
    var actualizaZonaResponseMutableData = MutableLiveData<ActualizaZonaResponse>()
    var consultaZonaResponseMutableData = MutableLiveData<ConsultaZonaResponse>()


    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getBusquedaZonas(busquedaZonaRequest: BusquedaZonaRequest) {
        isLoading.postValue(true)
        BusquedaZonaRepository().getBusquedaZonas(busquedaZonaRequest, busquedaZonaResponseMutableData, isLoading,codeError)
    }
    fun getAltaZona(altaZonaRequest: AltaZonaRequest) {
        isLoading.postValue(true)
        AltaZonasRepository().getAltaZonas(altaZonaRequest, altaZonaResponseMutableData, isLoading,codeError)
    }
    fun getEliminaZona(eliminaZonaRequest: EliminaZonaRequest) {
        isLoading.postValue(true)
        EliminaZonaRepository().getEliminaZonas(eliminaZonaRequest, eliminaZonaResponseMutableData, isLoading,codeError)
    }
    fun getActualizaZona(actualizaZonaRequest: ActualizaZonaRequest) {
        isLoading.postValue(true)
        ActualizaZonaRepository().getActualizaZona(actualizaZonaRequest, actualizaZonaResponseMutableData, isLoading,codeError)
    }
    fun getConsultaZonas(consultaZonaRequest: ConsultaZonaRequest) {
        isLoading.postValue(true)
        ConsultaZonaRepository().getConsultaZonas(consultaZonaRequest, consultaZonaResponseMutableData, isLoading,codeError)
    }
}