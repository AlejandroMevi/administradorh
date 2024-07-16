package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.AltaUsuarioSupervisorRepository
import com.venturessoft.human.repository.BusquedaZonaRepository
import com.venturessoft.human.repository.ConsultaZonaRepository
import com.venturessoft.human.models.request.AltaUsuarioSupervisorRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.request.ConsultaZonaRequest
import com.venturessoft.human.models.response.AltaUsuarioSupervisorResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaZonaResponse

class RegisterSupervisorFragmentViewModel {
    var altaUsuarioSuperResponseMutableData = MutableLiveData<AltaUsuarioSupervisorResponse>()
    var consultaZonaResponseMutableData = MutableLiveData<ConsultaZonaResponse>()
    var busquedaZonaResponseMutableLiveData = MutableLiveData<BusquedaZonaResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getAltaUsuarioSuperService(altaUsuarioSupervisorRequest: AltaUsuarioSupervisorRequest) {
        isLoading.postValue(true)
        AltaUsuarioSupervisorRepository().getAltaUsuarioSuper(altaUsuarioSupervisorRequest, altaUsuarioSuperResponseMutableData, isLoading,codeError)
    }
    fun getConsultaZonasSuperService(consultaZonaRequest: ConsultaZonaRequest) {
        isLoading.postValue(true)
        ConsultaZonaRepository().getConsultaZonas(consultaZonaRequest, consultaZonaResponseMutableData, isLoading,codeError)
    }
    fun getBusquedaZonasSuperService(busquedaZonaRequest: BusquedaZonaRequest)
    {
        isLoading.postValue(true)
        BusquedaZonaRepository().getBusquedaZonas(busquedaZonaRequest,busquedaZonaResponseMutableLiveData,isLoading,codeError)
    }
}