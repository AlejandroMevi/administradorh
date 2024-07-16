package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ActualizarPasswordRepository
import com.venturessoft.human.models.request.ActualizarPasswordRequest
import com.venturessoft.human.models.response.ActualizarPasswordResponse

class ChangePasswordFragmentViewModel {
    var actualizarPasswordResponseMutableData = MutableLiveData<ActualizarPasswordResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getActualizarPasswordService(actualizarPasswordRequest: ActualizarPasswordRequest){
        isLoading.postValue(true)
        ActualizarPasswordRepository().getActualizarPassword(actualizarPasswordRequest,actualizarPasswordResponseMutableData,isLoading, codeError)
    }
}