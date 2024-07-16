package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.venturessoft.human.models.request.AltaCompaniaRequest
import com.venturessoft.human.models.request.AltaFacturacionRequest
import com.venturessoft.human.repository.AltaUsuarioAdminRepository
import com.venturessoft.human.repository.ConsultaTokenNoLoginRepository
import com.venturessoft.human.models.request.AltaUsuarioAdminRequest
import com.venturessoft.human.models.request.ConsultaTokeNoLoginRequest
import com.venturessoft.human.models.request.EliminarUsuario
import com.venturessoft.human.models.response.AltaCompaniaResponse
import com.venturessoft.human.models.response.AltaFacturacionResponse
import com.venturessoft.human.models.response.AltaUsuarioAdminResponse
import com.venturessoft.human.models.response.ConsultaTokeNoLoginResponse
import com.venturessoft.human.repository.AltaCompaniaRepository
import com.venturessoft.human.repository.AltaFacturacionRepository

class CreateAccountViewModel:ViewModel() {
    val altaUsuarioAdminResponseMutableData = MutableLiveData<AltaUsuarioAdminResponse?>(null)
    val consultaTokenNoLoginResponseMutableData = MutableLiveData<ConsultaTokeNoLoginResponse?>(null)
    val altaCompaniaResponseMutableData = MutableLiveData<AltaCompaniaResponse?>(null)
    val isLoading = MutableLiveData<Boolean>()
    val codeError = MutableLiveData<Int>()
    var dataUser = MutableLiveData<AltaUsuarioAdminRequest?>(null)
    init {
        isLoading.postValue(false)
    }
    fun getAltaUsuarioService(altaUsuarioAdminRequest: AltaUsuarioAdminRequest) {
        isLoading.postValue(true)
        AltaUsuarioAdminRepository().getAltaUsuarioAdmin(altaUsuarioAdminRequest, altaUsuarioAdminResponseMutableData, isLoading,codeError)
    }
    fun getEliminarUsuario(eliminarUsuario: EliminarUsuario) {
        isLoading.postValue(true)
        AltaUsuarioAdminRepository().getEliminarUsuario(eliminarUsuario, altaUsuarioAdminResponseMutableData, isLoading,codeError)
    }
    fun getConsultaTokenNoLogin(consultaTokeNoLoginRequest: ConsultaTokeNoLoginRequest) {
        isLoading.postValue(true)
        ConsultaTokenNoLoginRepository().getConsultaToken(consultaTokeNoLoginRequest, consultaTokenNoLoginResponseMutableData, isLoading,codeError)
    }
    fun getAltaCompaniaService(altaCompaniaRequest: AltaCompaniaRequest){
        isLoading.postValue(true)
        AltaCompaniaRepository().getAltaCompania(altaCompaniaRequest,altaCompaniaResponseMutableData,isLoading,codeError)
    }
}