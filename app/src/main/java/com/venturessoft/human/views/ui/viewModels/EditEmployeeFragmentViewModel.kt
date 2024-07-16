package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizaEmpleadoRepository
import com.venturessoft.human.repository.ConsultaEstacionRepository
import com.venturessoft.human.models.request.ActualizaEmpleadoRequest
import com.venturessoft.human.models.request.ConsultaEstacionRequest
import com.venturessoft.human.models.response.ActualizaEmpleadoResponse
import com.venturessoft.human.models.response.ConsultaEstacionResponse

class EditEmployeeFragmentViewModel {
    var actualizaEmpleadoResponseMutableData = MutableLiveData<ActualizaEmpleadoResponse>()
    var consultaEstacionResponseMutableData = MutableLiveData<ConsultaEstacionResponse>()
    var consultaEstacionEmployeeResponseMutableData = MutableLiveData<ConsultaEstacionResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }

    fun putActualizaEmpleadoService(actualizaEmpleadoRequest: ActualizaEmpleadoRequest,idCia:Long, idEmpleado:Long, idUsuario:Long) {
        isLoading.postValue(true)
        ActualizaEmpleadoRepository().putActualizaEmpleado(actualizaEmpleadoRequest, idCia,
            idEmpleado, idUsuario, actualizaEmpleadoResponseMutableData, isLoading,codeError)
    }
    fun getConsultaEstacion(consultaEstacionRequest: ConsultaEstacionRequest) {
        isLoading.postValue(true)
        ConsultaEstacionRepository().getConsultaEstacion(consultaEstacionRequest, consultaEstacionResponseMutableData, isLoading,codeError)
    }
    fun getConsultaEstacionAsignEmployee(consultaEstacionRequest: ConsultaEstacionRequest) {
        isLoading.postValue(true)
        ConsultaEstacionRepository().getConsultaEstacion(consultaEstacionRequest, consultaEstacionEmployeeResponseMutableData, isLoading,codeError)
    }
}