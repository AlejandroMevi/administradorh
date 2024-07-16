package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.CambiarNipVinculadoRepository
import com.venturessoft.human.models.request.CambiarNipRequest
import com.venturessoft.human.models.response.CambiarNipResponse

class ChangeNipVinculadoViewModel {

    var cambiarNipResponseMutableData = MutableLiveData<CambiarNipResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getCambiarNip(cambiarNipRequest: CambiarNipRequest){
        isLoading.postValue(true)
        CambiarNipVinculadoRepository().getCambiarNip(cambiarNipRequest,cambiarNipResponseMutableData,isLoading, codeError)
    }
}