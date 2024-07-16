package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentEnrollUnenrollReportBinding
import com.venturessoft.human.models.request.ReporteEnrollRequest
import com.venturessoft.human.models.response.ReporteEnrollResponse
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.viewModels.ReporteEnrollViewModel
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class Enroll_Unenroll_ReportFragment : Fragment(), CoroutineScope {

    private lateinit var binding: FragmentEnrollUnenrollReportBinding
    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main
    private var reporteEnrollViewModel = ReporteEnrollViewModel()
    private var reporteEnrollResponse = ReporteEnrollResponse()
    private var mListener: OnFragmentInteractionListener? = null
    private var dateFormatIn: String? = null
    private var dateFormatUp: String? = null
    private var fileName: String? = "report"
    private var openDateFrom: Boolean = true
    private var openDateUp: Boolean = true
    private var statusEnroll: Boolean = true
    private var languageOfReport: String = "EN"
    lateinit var myDataBase: BDPayments
    private var mainInterface: MainInterface? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mJob = Job()
        myDataBase = BDPayments(requireActivity())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEnrollUnenrollReportBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backMenuToReport")
        Preferences.getUser(requireActivity().applicationContext)
        loadLanguageReport()
        listenButtons()
        addObservers()
        loadCalendarFrom()
        loadCalendarUp()
    }
    private fun addObservers() {
        reporteEnrollViewModel.reporteEnrollResponseMutableData.observe(viewLifecycleOwner) {
            reporteEnrollResponse = it
            if (reporteEnrollResponse.codigo == "ET000") {
                showLoadingAnimation()
                launch(Dispatchers.IO) {
                    convertB64ToPDF()
                }
            } else {
                Utilities.loadMessageError(
                    reporteEnrollResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        reporteEnrollViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
    }
    private suspend fun convertB64ToPDF() {
        val result = loadNextViewReport()
        withContext(Dispatchers.Main) {
            if (result == "OK") {
                loadFragment(ReportViewFragment())
            } else {
                Toast.makeText(requireActivity(), result, Toast.LENGTH_SHORT).show()
            }
            hideLoadingAnimation()
        }
    }
    private fun loadNextViewReport(): String? {
        return try {
            val reporteString = reporteEnrollResponse.file
            val urlBase = "${requireContext().filesDir}"
            val dwldsPath = File(urlBase + fileName.toString() + ".pdf")
            val pdfAsBytes: ByteArray = Base64.decode(reporteString, 0)
            val os = FileOutputStream(dwldsPath, false)
            os.write(pdfAsBytes)
            os.flush()
            os.close()
            "OK"
        } catch (e: Exception) {
            e.localizedMessage
        }
    }
    private fun listenButtons() {
        binding.btnBackMenuReport.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, ReportsMenuFragment(), "WelcomeFragment")
                    .commit()
        }
        binding.btnDownloadReport.setOnClickListener {
            when (User.ambiente) {
                "HU" -> {
                    val request = ReporteEnrollRequest(idCia = User.ciaVinculado, status = statusEnroll, fechaInicio = dateFormatIn!!, fechaFin = dateFormatUp!!, idioma = getString(R.string.languague).toUpperCase(), idUsuario = User.idUsuario)
                    reporteEnrollViewModel.getReporteEnrollVinService(request)
                }
                "ET" -> {
                    val request = ReporteEnrollRequest(idCia = User.scia!![0].cia, status = statusEnroll, fechaInicio = dateFormatIn!!, fechaFin = dateFormatUp!!, idioma = languageOfReport.toUpperCase(), idUsuario = User.idUsuario)
                    reporteEnrollViewModel.getReporteEnrollService(request)
                }
            }
        }
        binding.radioEmpEnroll.setOnClickListener {
            if (binding.radioEmpEnroll.isChecked ){
                statusEnroll = true
            }
        }
        binding.radioEmpUnenroll.setOnClickListener {
            if (binding.radioEmpUnenroll.isChecked) {
                statusEnroll = false
            }
        }
    }
    private fun loadLanguageReport() {
        val rows = myDataBase.getPorIdUser(User.idUsuario.toString())
        while (rows.moveToNext()) {
            languageOfReport = rows.getString(5)
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun loadCalendarFrom() {
        val format2 = SimpleDateFormat("yyyy-MM-dd")
        binding.dateFrom.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        dateFormatIn = format2.format(System.currentTimeMillis())
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val dateUpConvertDate: Date = SimpleDateFormat("dd/MM/yyyy").parse(binding.dateUp.text.toString())
            view.maxDate = System.currentTimeMillis()
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val dateObtained: Date = SimpleDateFormat("dd/MM/yyyy").parse(sdf.format(cal.time)) as Date
            if (dateObtained.compareTo(Date()).toString() == "-1" && dateObtained.compareTo(dateUpConvertDate).toString() != "1") {
                binding.dateFrom.setText(sdf.format(cal.time))
                dateFormatIn = format2.format(cal.time)
            }
            openDateFrom = true
        }
        binding.dateFrom.isLongClickable = false

        binding.dateFrom.setOnClickListener {
            if (openDateFrom) {
                openDateFrom = false
                val dialog = DatePickerDialog(requireActivity(), dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH))
                dialog.show()
                dialog.setOnCancelListener {
                    openDateFrom = true
                }
            }
        }

    }
    @SuppressLint("ClickableViewAccessibility")
    private fun loadCalendarUp() {
        val format2 = SimpleDateFormat("yyyy-MM-dd")
        binding.dateFrom.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        dateFormatUp = format2.format(System.currentTimeMillis())
        binding.dateUp.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            Calendar.getInstance()
            val dateObtained: Date = SimpleDateFormat("dd/MM/yyyy").parse(sdf.format(cal.time)) as Date
            val dateFromConvertDate: Date = SimpleDateFormat("dd/MM/yyyy").parse(binding.dateFrom.text.toString()) as Date
            if (dateObtained.compareTo(Date()).toString() == "-1") {
                binding.dateUp.setText(sdf.format(cal.time))
                dateFormatUp = format2.format(cal.time)
            }
            if (dateObtained.compareTo(dateFromConvertDate).toString() == "-1") {
                binding.dateUp.setText(sdf.format(Date()))
                dateFormatUp = format2.format(Date())
            }
            openDateUp = true
        }

        binding.dateUp.isLongClickable = false

        binding.dateUp.setOnClickListener {
            if (openDateUp) {
                openDateUp = false
                val dialog = DatePickerDialog(requireActivity(), dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH))
                dialog.show()
                dialog.setOnCancelListener {
                    openDateUp = true
                }
            }
        }
    }
    fun showLoadingAnimation() {
        binding.loadingAnimationEnroll.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationEnroll.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.reportsmenufragment_item_2))
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}