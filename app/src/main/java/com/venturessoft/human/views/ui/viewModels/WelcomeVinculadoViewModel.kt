package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.BuscarEmpVinculadoRepository
import com.venturessoft.human.models.request.BuscarEmpleadoVinRequest
import com.venturessoft.human.models.response.BuscarEmpleadoVinResponse

class WelcomeVinculadoViewModel {
    var buscaEmpleadoVinMutableData = MutableLiveData<BuscarEmpleadoVinResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }


    fun getBuscarEmpleadoVin(buscarEmpleadoVinRequest: BuscarEmpleadoVinRequest) {
        isLoading.postValue(true)
        BuscarEmpVinculadoRepository().getBusquedaEmpleadoVinculado(buscarEmpleadoVinRequest, buscaEmpleadoVinMutableData, isLoading, codeError)

    }
}