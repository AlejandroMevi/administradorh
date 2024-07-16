package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.*
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.ConsultaCompaniaRequest
import com.venturessoft.human.models.request.ConsultaFacturacionRequest
import com.venturessoft.human.models.request.ConsultaUsuarioRequest
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.ConsultaCompaniaResponse
import com.venturessoft.human.models.response.ConsultaFacturacionResponse
import com.venturessoft.human.models.response.ConsultaUsuarioResponse

class AdminDataFragmentViewModel {

    var consultaFacturacionResponseMutableData = MutableLiveData<ConsultaFacturacionResponse>()
    var consultaCompaniaResponseMutableData = MutableLiveData<ConsultaCompaniaResponse>()
    var consultUserResponseMutableData = MutableLiveData<ConsultaUsuarioResponse>()
    var actualizarUsuarioResponseMutableData = MutableLiveData<ActualizarUsuarioResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getConsultaUsuarioService(consultaUsuarioRequest: ConsultaUsuarioRequest) {
        isLoading.postValue(true)
        ConsultaUsuarioRepository().getConsultaUsuario(
            consultaUsuarioRequest,
            consultUserResponseMutableData,
            isLoading,
            codeError
        )
    }

    fun getActualizarUsuarioService(actualizarUsuarioRequest: ActualizarUsuarioRequest) {
        isLoading.postValue(true)
        ActualizarUsuarioRepository().getActualizarUsuario(actualizarUsuarioRequest, actualizarUsuarioResponseMutableData, isLoading, codeError)
    }

    fun getConsultaFacturacion(consultaFacturacionRequest: ConsultaFacturacionRequest) {
        isLoading.postValue(true)
        ConsultaFacturacionRepository().getConsultaFacturacion(
            consultaFacturacionRequest,
            consultaFacturacionResponseMutableData,
            isLoading,
            codeError
        )
    }

    fun getConsultaCompania(consultaCompaniaRequest: ConsultaCompaniaRequest) {
        isLoading.postValue(true)
        ConsultaCompaniaRepository().getConsultaCompania(
            consultaCompaniaRequest,
            consultaCompaniaResponseMutableData,
            isLoading,
            codeError
        )
    }

    fun getConsultaCompaniaData(consultaCompaniaRequest: ConsultaCompaniaRequest) {
        isLoading.postValue(true)
        ConsultaCompaniaDataRepository().getConsultaCompaniaData(
            consultaCompaniaRequest,
            consultaCompaniaResponseMutableData,
            isLoading,
            codeError
        )
    }
}