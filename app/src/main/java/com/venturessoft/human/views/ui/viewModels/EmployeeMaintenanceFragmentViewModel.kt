package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizaEmpleadoRepository
import com.venturessoft.human.repository.BusquedaEmpleadoRepository
import com.venturessoft.human.repository.ConsultaEmpleadoRepository
import com.venturessoft.human.repository.EliminaEmpleadoRepository
import com.venturessoft.human.models.request.ActualizaEmpleadoRequest
import com.venturessoft.human.models.request.BusquedaEmpleadoRequest
import com.venturessoft.human.models.request.ConsultaEmpleadoRequest
import com.venturessoft.human.models.request.EliminaEmpleadoRequest
import com.venturessoft.human.models.response.ActualizaEmpleadoResponse
import com.venturessoft.human.models.response.BusquedaEmpleadoResponse
import com.venturessoft.human.models.response.ConsultaEmpleadoResponse
import com.venturessoft.human.models.response.EliminaEmpleadoResponse

class EmployeeMaintenanceFragmentViewModel {
    var busquedaEmpleadoResponseMutableData = MutableLiveData<BusquedaEmpleadoResponse>()
    var busquedaEmpleadoNumResponseMutableData = MutableLiveData<ConsultaEmpleadoResponse>()
    var eliminaEmpleadoResponseMutableData = MutableLiveData<EliminaEmpleadoResponse>()
    var actualizaEmpleadoResponseMutableData = MutableLiveData<ActualizaEmpleadoResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }
    fun getBusquedaEmpleadoService(busquedaEmpleadoRequest: BusquedaEmpleadoRequest) {
        isLoading.postValue(true)
        BusquedaEmpleadoRepository().getBusquedaEmpleado(busquedaEmpleadoRequest, busquedaEmpleadoResponseMutableData, isLoading,codeError)
    }
    fun getBusquedaEmpleadoNumService(busquedaEmpleadoRequest: ConsultaEmpleadoRequest) {
        isLoading.postValue(true)
        ConsultaEmpleadoRepository().getConsultaEmpleado(busquedaEmpleadoRequest, busquedaEmpleadoNumResponseMutableData, isLoading,codeError)
    }
    fun getEliminaEmpleadoService(eliminaEmpleadoRequest: EliminaEmpleadoRequest) {
        isLoading.postValue(true)
        EliminaEmpleadoRepository().getEliminaEmpleado(eliminaEmpleadoRequest, eliminaEmpleadoResponseMutableData, isLoading,codeError)
    }
    fun putActualizaEmpleadoServices(actualizaEmpleadoRequest: ActualizaEmpleadoRequest, idCia:Long, idEmpleado:Long, idUsuario:Long) {
        isLoading.postValue(true)
        ActualizaEmpleadoRepository().putActualizaEmpleado(actualizaEmpleadoRequest, idCia,
            idEmpleado, idUsuario, actualizaEmpleadoResponseMutableData, isLoading,codeError)
    }
}