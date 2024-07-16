package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.OlvideNipVinculadoRepository
import com.venturessoft.human.models.request.OlvideNipRequest
import com.venturessoft.human.models.response.OlvideNipResponse

class ForgotNipVinculadoViewModel {
    var olvideNipResponseMutableData = MutableLiveData<OlvideNipResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()


    init {
        isLoading.postValue(false)
    }



    fun getForgetMyNip(olvideNipRequest: OlvideNipRequest){
        isLoading.postValue(true)
        OlvideNipVinculadoRepository().getOlvideNip(olvideNipRequest,olvideNipResponseMutableData,isLoading,codeError)
    }
}