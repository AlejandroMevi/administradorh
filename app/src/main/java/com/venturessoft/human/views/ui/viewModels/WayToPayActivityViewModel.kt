package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.AdministrarCompraRepository
import com.venturessoft.human.models.request.AdministrarCompraRequest
import com.venturessoft.human.models.response.AdministrarCompraResponse

class WayToPayActivityViewModel {
    var administrarCompraResponseMutableData = MutableLiveData<AdministrarCompraResponse>()
    var administrarCompraOfflineResponseMutableData = MutableLiveData<AdministrarCompraResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    var codeErrorOffline = MutableLiveData<Int>()
    init {
        isLoading.postValue(false)
    }

    fun getAdministrarCompra(administrarCompraRequest: AdministrarCompraRequest){
        isLoading.postValue(true)
        AdministrarCompraRepository().getAdministrarCompra(administrarCompraRequest,administrarCompraResponseMutableData,isLoading,codeErrorOffline)
    }
    fun getAdministrarCompraOffline(administrarCompraRequest: AdministrarCompraRequest){
        isLoading.postValue(true)
        AdministrarCompraRepository().getAdministrarCompra(administrarCompraRequest,administrarCompraOfflineResponseMutableData,isLoading,codeError)
    }
}