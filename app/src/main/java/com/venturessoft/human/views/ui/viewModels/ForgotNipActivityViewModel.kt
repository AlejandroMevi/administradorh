package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.RecuperarPasswordRepository
import com.venturessoft.human.models.request.RecuperarPasswordRequest
import com.venturessoft.human.models.response.RecuperarPasswordResponse

class ForgotNipActivityViewModel {
    var recuperarPasswordResponseMutableData = MutableLiveData<RecuperarPasswordResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }
    fun getRecuperarPasswordService(
        recuperarPasswordRequest: RecuperarPasswordRequest,
        token: String
    ){
        isLoading.postValue(true)
        RecuperarPasswordRepository().getRecuperarPassword(recuperarPasswordRequest,recuperarPasswordResponseMutableData,isLoading,codeError,token)
    }
}