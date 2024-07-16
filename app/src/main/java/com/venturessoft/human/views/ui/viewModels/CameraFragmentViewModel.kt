package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizaEmpleadoRepository
import com.venturessoft.human.repository.CambiarImagenVinculadoRepository
import com.venturessoft.human.models.request.ActualizaEmpleadoRequest
import com.venturessoft.human.models.request.EnrolarRequest
import com.venturessoft.human.models.response.ActualizaEmpleadoResponse
import com.venturessoft.human.models.response.EnrolarResponse

class CameraFragmentViewModel {

    var actualizaEmpleadoResponseMutableData = MutableLiveData<ActualizaEmpleadoResponse>()
    var actualizaEmpleadoVinculadoResponseMutableData = MutableLiveData<EnrolarResponse>()

    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }

    fun putActualizaEmpleadoService(actualizaEmpleadoRequest: ActualizaEmpleadoRequest, idCia:Long, idEmpleado:Long, idUsuario:Long) {
        isLoading.postValue(true)
        ActualizaEmpleadoRepository().putActualizaEmpleado(actualizaEmpleadoRequest, idCia,
            idEmpleado, idUsuario, actualizaEmpleadoResponseMutableData, isLoading,codeError)
    }

    fun getActualizaEmpleadoVinculadoService(enrolarRequest: EnrolarRequest) {
        isLoading.postValue(true)
        CambiarImagenVinculadoRepository().getCambiarImagen(enrolarRequest, actualizaEmpleadoVinculadoResponseMutableData, isLoading,codeError)
    }
}