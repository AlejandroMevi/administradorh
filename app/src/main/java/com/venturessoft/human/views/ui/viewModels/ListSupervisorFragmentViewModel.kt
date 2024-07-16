package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizarUsuarioRepository
import com.venturessoft.human.repository.BusquedaUsuarioRepository
import com.venturessoft.human.repository.ConsultaUsuarioRepository
import com.venturessoft.human.repository.EliminaUsuarioRepository
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.BusquedaUsuarioRequest
import com.venturessoft.human.models.request.ConsultaUsuarioRequest
import com.venturessoft.human.models.request.EliminaUsuarioRequest
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.BusquedaUsuarioResponse
import com.venturessoft.human.models.response.ConsultaUsuarioResponse
import com.venturessoft.human.models.response.EliminaUsuarioResponse

class ListSupervisorFragmentViewModel {
    var consultUserResponseMutableData = MutableLiveData<ConsultaUsuarioResponse>()
    var actualizarUsuarioResponseMutableData = MutableLiveData<ActualizarUsuarioResponse>()
    var eliminaUsuarioResponseMutableData = MutableLiveData<EliminaUsuarioResponse>()
    var busquedaUsuarioResponseMutableData = MutableLiveData<BusquedaUsuarioResponse>()
    var busquedaUsuarioFilterResponseMutableData = MutableLiveData<BusquedaUsuarioResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getConsultaUsuarioService(consultaUsuarioRequest: ConsultaUsuarioRequest){
        isLoading.postValue(true)
        ConsultaUsuarioRepository().getConsultaUsuario(consultaUsuarioRequest,consultUserResponseMutableData,isLoading,codeError)
    }

    fun getActualizarUsuarioService(actualizarUsuarioRequest: ActualizarUsuarioRequest){
        isLoading.postValue(true)
        ActualizarUsuarioRepository().getActualizarUsuario(actualizarUsuarioRequest,actualizarUsuarioResponseMutableData,isLoading,codeError)
    }

    fun getEliminaUsuarioService(eliminaUsuarioRequest: EliminaUsuarioRequest) {
        isLoading.postValue(true)
        EliminaUsuarioRepository().getEliminaUsuario(eliminaUsuarioRequest, eliminaUsuarioResponseMutableData, isLoading,codeError)
    }

    fun getBuscarSuperService(busquedaUsuarioRequest: BusquedaUsuarioRequest) {
        isLoading.postValue(true)
        BusquedaUsuarioRepository().getBusquedaUsuario(busquedaUsuarioRequest, busquedaUsuarioResponseMutableData, isLoading,codeError)
    }


}