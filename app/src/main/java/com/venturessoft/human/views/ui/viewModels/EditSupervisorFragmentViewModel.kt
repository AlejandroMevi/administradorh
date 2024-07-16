package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizarUsuarioRepository
import com.venturessoft.human.repository.BusquedaZonaAdmiRepository
import com.venturessoft.human.repository.BusquedaZonaRepository
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.BusquedaZonaAdmiResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaZonaResponse

class EditSupervisorFragmentViewModel {



    var busquedaZonaResponseMutableData = MutableLiveData<BusquedaZonaResponse>()
    var busquedaZonaAdmiResponseMutableData = MutableLiveData<BusquedaZonaAdmiResponse>()
    var busquedaZonaAdmResponseMutableData = MutableLiveData<BusquedaZonaResponse>()

    var consultaZonaAdminResponseMutableData = MutableLiveData<ConsultaZonaResponse>()
    var actualizarUsuarioResponseMutableData = MutableLiveData<ActualizarUsuarioResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }




    fun getBusquedaZonas(busquedaZonaRequest: BusquedaZonaRequest) {
        isLoading.postValue(true)
        BusquedaZonaRepository().getBusquedaZonas(busquedaZonaRequest, busquedaZonaResponseMutableData, isLoading,codeError)
    }
    fun getConsultaZonasAdmin(busquedaZonaRequest: BusquedaZonaRequest) {
        isLoading.postValue(true)
        BusquedaZonaAdmiRepository().getConsultaZonasAdmin(busquedaZonaRequest, busquedaZonaAdmiResponseMutableData, isLoading,codeError)
    }
    fun getActualizarUsuarioService(actualizarUsuarioRequest: ActualizarUsuarioRequest){
        isLoading.postValue(true)
        ActualizarUsuarioRepository().getActualizarUsuario(actualizarUsuarioRequest,actualizarUsuarioResponseMutableData,isLoading,codeError)
    }
}