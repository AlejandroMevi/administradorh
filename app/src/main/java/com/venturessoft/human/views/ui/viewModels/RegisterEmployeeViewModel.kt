package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.AltaEmpleadoRepository
import com.venturessoft.human.repository.ConsultaEstacionRepository
import com.venturessoft.human.models.request.AltaEmpleadoRequest
import com.venturessoft.human.models.request.ConsultaEstacionRequest
import com.venturessoft.human.models.response.AltaEmpleadoResponse
import com.venturessoft.human.models.response.ConsultaEstacionResponse

class RegisterEmployeeViewModel  {
    var altaEmpleadoResponseMutableData = MutableLiveData<AltaEmpleadoResponse>()
    var consultaEstacionResponseMutableData = MutableLiveData<ConsultaEstacionResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }
    fun getAltaEmpleadoService(altaEmpleadoRequest: AltaEmpleadoRequest) {
        isLoading.postValue(true)
        AltaEmpleadoRepository().getAltaEmpleado(altaEmpleadoRequest, altaEmpleadoResponseMutableData, isLoading,codeError)
    }
    fun getConsultaEstacion(consultaEstacionRequest: ConsultaEstacionRequest) {
        isLoading.postValue(true)
        ConsultaEstacionRepository().getConsultaEstacion(consultaEstacionRequest, consultaEstacionResponseMutableData, isLoading,codeError)
    }
}