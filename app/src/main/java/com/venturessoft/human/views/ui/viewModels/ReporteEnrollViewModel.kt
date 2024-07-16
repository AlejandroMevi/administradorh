package com.venturessoft.human.views.ui.viewModels

import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.repository.ReporteEnrollRepository
import com.venturessoft.human.models.request.ReporteEnrollRequest
import com.venturessoft.human.models.response.ReporteEnrollResponse

class ReporteEnrollViewModel {
    var reporteEnrollResponseMutableData = MutableLiveData<ReporteEnrollResponse>()
    var isLoading = MutableLiveData<Boolean>()
    var codeError = MutableLiveData<Int>()
    fun getReporteEnrollService(reporteEnrollRequest: ReporteEnrollRequest) {
        isLoading.postValue(true)
        ReporteEnrollRepository().getReporteEnroll(reporteEnrollRequest, reporteEnrollResponseMutableData, isLoading,codeError)
    }
    fun getReporteEnrollVinService(reporteEnrollRequest: ReporteEnrollRequest) {
        isLoading.postValue(true)
        ReporteEnrollRepository().getReporteEnrollVin(reporteEnrollRequest, reporteEnrollResponseMutableData, isLoading,codeError)
    }
}