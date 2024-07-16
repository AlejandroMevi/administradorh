package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.Repository.StaticalConsultationRepository
import com.venturessoft.human.models.response.EmpleadosPayResponse
import com.venturessoft.human.models.request.BuscarEmpleadoVinRequest
import com.venturessoft.human.models.request.ConsultaEmpleadoRequest
import com.venturessoft.human.models.request.ConsultaEstadisticaRequest
import com.venturessoft.human.models.request.ConsultaImagenVinRequest
import com.venturessoft.human.models.response.BuscarEmpleadoVinResponse
import com.venturessoft.human.models.response.ConsultaEmpleadoResponse
import com.venturessoft.human.models.response.ConsultaEstadisticaResponse
import com.venturessoft.human.models.response.ConsultaImagenVinResponse
import com.venturessoft.human.repository.BuscarEmpVinculadoRepository
import com.venturessoft.human.repository.ConsultaEmpleadoRepository
import com.venturessoft.human.repository.ConsultaImagenVinRepository

class WelcomeViewModel {
    var saldoResponseMutableData = MutableLiveData<EmpleadosPayResponse>()
    var consultationResponseMutableData = MutableLiveData<ConsultaEstadisticaResponse>()
    var consultaEmpleadoResponseMutableData = MutableLiveData<ConsultaEmpleadoResponse>()
    var buscarEmpleadoVinMutableData = MutableLiveData<BuscarEmpleadoVinResponse>()
    var consultaImagenVinMutableData = MutableLiveData<ConsultaImagenVinResponse>()


    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }



    fun getStatisticalConsultationService(statisticalConsultationRequest: ConsultaEstadisticaRequest){
       isLoading.postValue(true)
        StaticalConsultationRepository().getStaticalConsultation(statisticalConsultationRequest,consultationResponseMutableData,isLoading,codeError)

    }

    fun getConsultaEmpleadoService(consultaEmpleadoRequest: ConsultaEmpleadoRequest){
        isLoading.postValue(true)
        ConsultaEmpleadoRepository().getConsultaEmpleado(consultaEmpleadoRequest,consultaEmpleadoResponseMutableData,isLoading,codeError)

    }

    fun getSaldoService(idCompania: Long,){
        isLoading.postValue(true)
        StaticalConsultationRepository().getSaldo(idCompania,saldoResponseMutableData,isLoading,codeError)

    }
    fun getBuscarEmpleadoVin(buscarEmpleadoVinRequest: BuscarEmpleadoVinRequest){
        isLoading.postValue(true)
        BuscarEmpVinculadoRepository().getBusquedaEmpleadoVinculado(buscarEmpleadoVinRequest,buscarEmpleadoVinMutableData,isLoading,codeError)
    }
    fun getConsultaImagen(consultaImagenVinRequest:  ConsultaImagenVinRequest){
        isLoading.postValue(true)
        ConsultaImagenVinRepository().getConsultaImagen(consultaImagenVinRequest, consultaImagenVinMutableData, isLoading, codeError)
    }

}