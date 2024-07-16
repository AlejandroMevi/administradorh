package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.*
import com.venturessoft.human.models.request.*
import com.venturessoft.human.models.response.*

class LoginViewModel {
    var userAccessResponseMutableData = MutableLiveData<UserAccesoResponse>()
    var consultaCompaniaResponseMutableData = MutableLiveData<ConsultaCompaniaResponse>()
    var actualizarUsuarioResponseMutableData = MutableLiveData<ActualizarUsuarioResponse>()
    var actualizarTokenResponseMutableData = MutableLiveData<ActualizarUsuarioResponse>()
    var aceptarAvisoResponseMutableLiveData = MutableLiveData<AceptarAvisoResponse>()
    var getAvisoResponseMutableLiveData = MutableLiveData<GetAvisoPrivacidadResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    var codeErrorToken = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }



    fun getuserAccessService(userAccessRequest:UserAccessRequest){
        isLoading.postValue(true)
        UserAccessRepository().getUserAccess(userAccessRequest,userAccessResponseMutableData,isLoading,codeError)
    }
    fun getConsultaCompania(consultaCompaniaRequest: ConsultaCompaniaRequest) {
        isLoading.postValue(true)
        ConsultaCompaniaRepository().getConsultaCompania(consultaCompaniaRequest, consultaCompaniaResponseMutableData, isLoading,codeError)
    }
    fun getActualizarUsuarioService(actualizarUsuarioRequest: ActualizarUsuarioRequest){
        isLoading.postValue(true)
        ActualizarUsuarioRepository().getActualizarUsuario(actualizarUsuarioRequest,actualizarUsuarioResponseMutableData,isLoading,codeError)
    }
    fun getActualizarTokenService(actualizarUsuarioRequest: ActualizarUsuarioRequest){
        isLoading.postValue(true)
        ActualizarUsuarioRepository().getActualizarUsuario(actualizarUsuarioRequest,actualizarTokenResponseMutableData,isLoading,codeErrorToken)
    }
    fun getAvisoService(getAvisoPrivacidadRequest: GetAvisoPrivacidadRequest){
        isLoading.postValue(true)
        GetAvisoRepository().getAviso(getAvisoPrivacidadRequest,getAvisoResponseMutableLiveData,isLoading,codeError)
    }
    fun aceptarAvisoService(aceptaAvisoRequest: AceptaAvisoRequest){
        isLoading.postValue(true)
        AceptaAvisoRepository().getAceptaAviso(aceptaAvisoRequest,aceptarAvisoResponseMutableLiveData,isLoading,codeError)
    }
}