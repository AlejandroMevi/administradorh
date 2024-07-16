package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.DetalleAuditoriaRepository
import com.venturessoft.human.models.request.DetallesRequest
import com.venturessoft.human.models.response.DetallesResponse

class DetalleAuditHistoryViewModel {
    var detalleAuditMutableData = MutableLiveData<DetallesResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    fun getDetalleAuditHistory(detallesRequest: DetallesRequest) {
        isLoading.postValue(true)
        DetalleAuditoriaRepository().getDetallesHistorial(detallesRequest, detalleAuditMutableData, isLoading,codeError)
    }


}