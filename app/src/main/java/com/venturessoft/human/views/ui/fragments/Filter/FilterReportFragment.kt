package com.venturessoft.human.views.ui.fragments.Filter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentFilterReportBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.models.request.ReporteChecadasRequest
import com.venturessoft.human.models.request.ReporteOpcionRequest
import com.venturessoft.human.models.response.ReporteChecadasResponse
import com.venturessoft.human.models.response.ReporteOpcionResponse
import com.venturessoft.human.models.response.empleado
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReporByEmployee
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportDetailsStationFree
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportFragment
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportsMenuFragment
import com.venturessoft.human.views.ui.viewModels.FilterReportFragmentViewModel
import java.text.SimpleDateFormat
import java.util.*

class FilterReportFragment : androidx.fragment.app.Fragment() {
    private lateinit var binding: FragmentFilterReportBinding
    private var filterReportFragmentViewModel = FilterReportFragmentViewModel()
    private var reporteChecadasResponse = ReporteChecadasResponse()
    private var reporteOpcionResponse = ReporteOpcionResponse()
    private var typeOfReport: String = "Email"
    private var languageOfReport: String = "EN"
    private var mListener: OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null
    var user: User? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    private var selectReport: String? = null
    private var dateFormatIn: String? = null
    private var dateFormatUp: String? = null
    private var openDateFrom: Boolean = true
    private var openDateUp: Boolean = true
    companion object {
        private const val ARG_USER = "userInfo"
        var empleado: ArrayList<empleado>? = null
        lateinit var myDataBase: BDPayments

        fun newInstance(loggedUser: User): FilterReportFragment {
            val fragment = FilterReportFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, loggedUser)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataBase = BDPayments(requireActivity())
        val data = this.arguments
        if (data != null) {
            this.selectReport = data.getString("ReportSelect")
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFilterReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun addObservers() {
        filterReportFragmentViewModel.reporteChecadasResponseMutableData.observe(viewLifecycleOwner) {
            reporteChecadasResponse = it
            if (reporteChecadasResponse.codigo == "ET000") {
                loadNextViewReport()
            } else {
                Utilities.loadMessageError(
                    reporteChecadasResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        filterReportFragmentViewModel.reporteChecadasFromResponseMutableData.observe(viewLifecycleOwner) {
            if (it.codigo == "ET000") {
                successReportDateFrom()
            } else {
                Utilities.loadMessageError(it.codigo, requireActivity(), childFragmentManager)
            }
        }
        filterReportFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        filterReportFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        filterReportFragmentViewModel.reporteopcionResponseMutableData.observe(viewLifecycleOwner) {
            reporteOpcionResponse = it
            if (reporteOpcionResponse.codigo == "ET000") {
                successReporteOpcion()
            } else {
                Utilities.loadMessageError(it.codigo, requireActivity(), childFragmentManager)
            }
        }
    }
    private fun successReportDateFrom() {
        val reportFragment = ReportFragment()
        val containerData = Bundle()
        containerData.putString("dateFrom", dateFormatIn)
        containerData.putString("dateUp", dateFormatUp)
        reportFragment.arguments = containerData
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, reportFragment, "WelcomeFragment")
                .commit()
    }
    private fun loadNextViewReport() {
        val reporByEmployee = ReporByEmployee()
        val containerData = Bundle()
        containerData.putString("numEmployee", binding.numEmploye.text.toString())
        containerData.putString("nameEmploye", reporteChecadasResponse.empleado!![0].nombre!!.toString())
        containerData.putString("apPaterno", reporteChecadasResponse.empleado!![0].apellidoPaterno!!.toString())
        if (reporteChecadasResponse.empleado!![0].apellidoMaterno!!.isNotEmpty()) {
            containerData.putString("apMaterno", reporteChecadasResponse.empleado!![0].apellidoMaterno!!.toString())
        }
        containerData.putString("dateFrom", dateFormatIn)
        containerData.putString("dateUp", dateFormatUp)
        reporByEmployee.arguments = containerData
        empleado = reporteChecadasResponse.empleado
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, reporByEmployee, "WelcomeFragment")
                .commit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadCalendarFrom()
        loadCalendarUp()
        addObservers()
        mListener?.onBack("backReports")
        user = Preferences.getUser(requireActivity().applicationContext)
        loadDataBase()
        listenButtons()
    }
    private fun listenButtons() {
        binding.btnAtrReport.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, ReportsMenuFragment(), "WelcomeFragment")
                    .commit()
        }
        binding.btnApply.setOnClickListener {
            val dia = binding.dateFrom.text.toString().substring(0, 2)
            val mes = binding.dateFrom.text.toString().substring(3, 5)
            val año = binding.dateFrom.text.toString().substring(6, 10)
            when (com.venturessoft.human.utils.User.ambiente) {
                "HU" -> {
                    if (this.selectReport != null && this.selectReport.equals(getString(R.string.reportsmenufragment_item_3))) {
                        val fragmentCall = ReportDetailsStationFree()
                        val containerData = Bundle()
                        containerData.putString("fechaFin", binding.dateUp.text.toString().substring(6, 10) + "-" + binding.dateUp.text.toString().substring(3, 5) + "-" + binding.dateUp.text.toString().substring(0, 2)) //LocalDate.parse(dateUp.text.toString()).format(DateTimeFormatter.ofPattern("yyy-MM-dd")))
                        containerData.putString("fechaInicio", binding.dateFrom.text.toString().substring(6, 10) + "-" + binding.dateFrom.text.toString().substring(3, 5) + "-" + binding.dateFrom.text.toString().substring(0, 2))
                        containerData.putString("numCia", com.venturessoft.human.utils.User.ciaVinculado.toString())
                        containerData.putInt("numEmp", if (binding.numEmploye.text.isNullOrEmpty()) 0 else binding.numEmploye.text.toString().toInt())
                        fragmentCall.arguments = containerData
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, fragmentCall, "WelcomeFragment")
                            .commit()

                    } else {
                        if (binding.checkBoxAllEmployees.isChecked) {
                            typeOfReport = "Email"
                            val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario,
                                idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.ciaVinculado, fechaInicio = dateFormatIn!!,
                                fechaFin = dateFormatUp!!, opcion = typeOfReport, idioma = getString(R.string.languague).toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.ciaVinculado)
                            filterReportFragmentViewModel.getReporteFileService(request)
                        } else {
                            if (binding.numEmploye.text.toString().isBlank().not()) {
                                val request = ReporteChecadasRequest(
                                    dateFormatIn!!,
                                    dateFormatUp!!,
                                    binding.numEmploye.text.toString().toLong(),
                                    idCia = com.venturessoft.human.utils.User.ciaVinculado,
                                    idUsuario = com.venturessoft.human.utils.User.idUsuario
                                )
                                filterReportFragmentViewModel.getReporteChecadaService(request)
                            } else {
                                val newDate = "$año-$mes-$dia"
                                val request = ReporteChecadasRequest(
                                    fechaInicio = newDate,
                                    fechaFin = newDate,
                                    idCia = com.venturessoft.human.utils.User.ciaVinculado,
                                    idUsuario = com.venturessoft.human.utils.User.idUsuario
                                )
                                filterReportFragmentViewModel.getReporteChecadaDateFromService(request)
                            }
                        }
                    }
                }
                "ET"->{
                    if (this.selectReport != null && this.selectReport.equals(getString(R.string.reportsmenufragment_item_3))) {
                        val fragmentCall = ReportDetailsStationFree()
                        val containerData = Bundle()
                        containerData.putString("fechaFin", binding.dateUp.text.toString().substring(6, 10) + "-" + binding.dateUp.text.toString().substring(3, 5) + "-" + binding.dateUp.text.toString().substring(0, 2)) //LocalDate.parse(dateUp.text.toString()).format(DateTimeFormatter.ofPattern("yyy-MM-dd")))
                        containerData.putString("fechaInicio", binding.dateFrom.text.toString().substring(6, 10) + "-" + binding.dateFrom.text.toString().substring(3, 5) + "-" + binding.dateFrom.text.toString().substring(0, 2))
                        containerData.putString("numCia", com.venturessoft.human.utils.User.scia!![0].cia.toString())
                        containerData.putInt("numEmp", if (binding.numEmploye.text.isNullOrEmpty()) 0 else binding.numEmploye.text.toString().toInt())
                        fragmentCall.arguments = containerData
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.Fragpricipal, fragmentCall, "WelcomeFragment")
                            .commit()
                    } else {
                        if (binding.checkBoxAllEmployees.isChecked) {
                            typeOfReport = "Email"
                            val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario,
                                idCia = com.venturessoft.human.utils.User.ciaSeleccionada?:com.venturessoft.human.utils.User.scia!![0].cia, fechaInicio = dateFormatIn!!,
                                fechaFin = dateFormatUp!!, opcion = typeOfReport, idioma = getString(R.string.languague).toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia)
                            filterReportFragmentViewModel.getReporteFileService(request)
                        } else {
                            if (binding.numEmploye.text.toString().isBlank().not()) {
                                val request = ReporteChecadasRequest(
                                    dateFormatIn!!,
                                    dateFormatUp!!,
                                    binding.numEmploye.text.toString().toLong(),
                                    com.venturessoft.human.utils.User.scia!![0].cia,
                                    idUsuario = com.venturessoft.human.utils.User.idUsuario
                                )
                                filterReportFragmentViewModel.getReporteChecadaService(request)
                            } else {
                                val newDate = "$año-$mes-$dia"
                                val request = ReporteChecadasRequest(
                                    fechaInicio = newDate,
                                    fechaFin = newDate,
                                    idCia = com.venturessoft.human.utils.User.scia!![0].cia
                                )
                                filterReportFragmentViewModel.getReporteChecadaDateFromService(request)
                            }
                        }
                    }
                }
            }
        }
        binding.checkBoxAllEmployees.setOnClickListener {
            if (binding.checkBoxAllEmployees.isChecked) {
                binding.tilNumEmploye.isVisible = false
            } else {
                binding.tilNumEmploye.isVisible = true
                binding.numEmploye.setText("")
            }
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
            val dateUpConvertDate: Date = SimpleDateFormat("dd/MM/yyyy").parse(binding.dateUp.text.toString()) as Date
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

        binding.dateFrom.setOnTouchListener { v, event ->
            if (openDateFrom) {
                openDateFrom = false
                val dialog = DatePickerDialog(
                    requireActivity(), dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                dialog.show()
                dialog.setOnCancelListener {
                    openDateFrom = true
                }
            }
            v?.onTouchEvent(event) ?: true
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
        binding.dateUp.setOnTouchListener { v, event ->
            if (openDateUp) {
                openDateUp = false
                val dialog = DatePickerDialog(
                    requireActivity(), dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                dialog.show()
                dialog.setOnCancelListener {
                    openDateUp = true
                }
            }
            v?.onTouchEvent(event) ?: true
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
    fun showLoadingAnimation() {
        binding.loadingAnimationFilter.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationFilter.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(requireActivity().getString(identificadorMensaje), requireActivity(),childFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }
    private fun successReporteOpcion() {
        when (typeOfReport) {
            "Email" -> {
                val intent = Intent(activity, AnimotionLottie::class.java)
                intent.putExtra("Redirect", "EmailAnimation")
                startActivity(intent)
            }
            "Csv" -> {
                val intent = Intent(activity, AnimotionLottie::class.java)
                intent.putExtra("Redirect", "ExcelAnimation")
                startActivity(intent)
            }
            "Pdf" -> {
                val intent = Intent(activity, AnimotionLottie::class.java)
                intent.putExtra("Redirect", "ExcelAnimation")
                startActivity(intent)
            }
        }
    }
    private fun loadDataBase() {
        val rows = myDataBase.getPorIdUser(com.venturessoft.human.utils.User.idUsuario.toString())
        while (rows.moveToNext()) {
            languageOfReport = rows.getString(5)
        }
    }
    override fun onResume() {
        super.onResume()
        if (this.selectReport != null && this.selectReport.equals(getString(R.string.reportsmenufragment_item_3))) {
            mainInterface?.setTextToolbar(getString(R.string.reportsmenufragment_item_3))
        }else{
            mainInterface?.setTextToolbar(getString(R.string.reportsmenufragment_item_1))
        }
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}
