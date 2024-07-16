package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.*
import com.venturessoft.human.models.request.*
import com.venturessoft.human.models.response.*

class CompanyDataFragmentViewModel {
    var altaFacturacionResponseMutableData = MutableLiveData<AltaFacturacionResponse?>()
    var  consultaFacturacionResponseMutableData = MutableLiveData<ConsultaFacturacionResponse>()

    var  consultaCompaniaResponseMutableData = MutableLiveData<ConsultaCompaniaResponse>()
    var actualizarFacturacionResponseMutableData = MutableLiveData<ActualizaFacturacionResponse>()
    var actualizarCompaniaResponseMutableData = MutableLiveData<ActualizaCompaniaResponse>()


    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getConsultaFacturacion(consultaFacturacionRequest: ConsultaFacturacionRequest) {
        isLoading.postValue(true)
        ConsultaFacturacionRepository().getConsultaFacturacion(consultaFacturacionRequest, consultaFacturacionResponseMutableData, isLoading,codeError)
    }

    fun getConsultaCompania(consultaCompaniaRequest: ConsultaCompaniaRequest) {
        isLoading.postValue(true)
        ConsultaCompaniaRepository().getConsultaCompania(consultaCompaniaRequest, consultaCompaniaResponseMutableData, isLoading,codeError)
    }
    fun getConsultaCompaniaData(consultaCompaniaRequest: ConsultaCompaniaRequest) {
        isLoading.postValue(true)
        ConsultaCompaniaDataRepository().getConsultaCompaniaData(consultaCompaniaRequest, consultaCompaniaResponseMutableData, isLoading,codeError)
    }
    fun getActualizarFacturacionService(actualizaFacturacionRequest: ActualizaFacturacionRequest){
        isLoading.postValue(true)
        ActualizaFacturacionRepository().getActualizarFacturacion(actualizaFacturacionRequest,actualizarFacturacionResponseMutableData,isLoading, codeError)
    }
    fun getActualizarCompaniaService(actualizaCompaniaRequest: ActualizaCompaniaRequest){
        isLoading.postValue(true)
        ActualizaCompaniaRepository().getActualizarCompania(actualizaCompaniaRequest,actualizarCompaniaResponseMutableData,isLoading, codeError)
    }
    fun getFacturacionService(altaFacturacionRequest: AltaFacturacionRequest) {
        isLoading.postValue(true)
        AltaFacturacionRepository().getAltaFacturacion(altaFacturacionRequest, altaFacturacionResponseMutableData, isLoading,codeError)
    }
}