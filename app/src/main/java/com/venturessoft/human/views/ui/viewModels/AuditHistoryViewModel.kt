package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ListaAuditoriaRepository
import com.venturessoft.human.models.request.ListaAuditoriaRequest
import com.venturessoft.human.models.response.ListaAuditoriaResponse

class AuditHistoryViewModel {
    var listaAuditoriaMutableData = MutableLiveData<ListaAuditoriaResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    fun getListAuditoriaService(listaAuditoriaRequest: ListaAuditoriaRequest) {
        isLoading.postValue(true)
        ListaAuditoriaRepository().getListaHistorial(listaAuditoriaRequest, listaAuditoriaMutableData, isLoading,codeError)
    }
}