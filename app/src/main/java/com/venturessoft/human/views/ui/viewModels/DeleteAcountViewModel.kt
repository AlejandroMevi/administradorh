package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.models.Response.DeleteAcountResponse
import com.venturessoft.human.models.request.DeleteAccountRequest
import com.venturessoft.human.repository.DeleteAcountRepository

class DeleteAcountViewModel {
    var deleteAcountResponseMutableData = MutableLiveData<DeleteAcountResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()

    init {
        isLoading.postValue(false)
    }

    fun getDeleteAcountServices(deleteAccountRequest: DeleteAccountRequest) {
        isLoading.postValue(true)
        DeleteAcountRepository().getDeleteAcount(deleteAccountRequest, deleteAcountResponseMutableData, isLoading,codeError)
    }
}