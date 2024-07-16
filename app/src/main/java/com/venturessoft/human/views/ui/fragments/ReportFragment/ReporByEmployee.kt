package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentReporByEmployeeBinding
import com.venturessoft.human.models.request.ReporteChecadasRequest
import com.venturessoft.human.models.request.ReporteOpcionRequest
import com.venturessoft.human.models.response.ReporteChecadasResponse
import com.venturessoft.human.models.response.ReporteOpcionResponse
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.User
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.AdapterReportEmployee
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.activities.SwipeToDeleteCallback
import com.venturessoft.human.views.ui.fragments.Filter.FilterReportFragment
import com.venturessoft.human.views.ui.viewModels.ReportByEmployeeViewModel

class ReporByEmployee : Fragment() {
    private lateinit var binding : FragmentReporByEmployeeBinding
    private var mListener: OnFragmentInteractionListener? = null
    private var reporteOpcionResponse = ReporteOpcionResponse()
    lateinit var simpleAdapter: AdapterReportEmployee
    private var reportByEmployeeViewModel = ReportByEmployeeViewModel()
    private var reporteChecadasResponse = ReporteChecadasResponse()
    var user: User? = null
    private var typeOfReport: String = ""
    private var languageOfReport: String = "EN"
    private var numEmpleado: String = ""
    private var dateFrom: String = ""
    private var dateUp: String = ""
    private var nEmployee: String? = ""
    var nameEmployee: String? = ""
    var apPaterno: String? = ""
    var apMaterno: String? = ""

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        lateinit var myDataBase: BDPayments
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataBase = BDPayments(requireActivity())
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReporByEmployeeBinding.inflate(inflater, container, false)
        binding.employeereportrecicler.addItemDecoration(DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL))
        binding.employeereportrecicler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity())
        loadDataBase()
        loadDataDefault()
        loadObserver()
        loadSuccesReport()
        return binding.root
    }

    private fun loadObserver() {
        reportByEmployeeViewModel.reporteChecadasResponseMutableData.observe(viewLifecycleOwner) {
            reporteChecadasResponse = it
            if (reporteChecadasResponse.codigo == "ET000") {
            } else {
                Utilities.loadMessageError(
                    reporteChecadasResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        reportByEmployeeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        reportByEmployeeViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        reportByEmployeeViewModel.reporteopcionResponseMutableData.observe(viewLifecycleOwner) {
            reporteOpcionResponse = it
            if (it.codigo == "ET000") {
                successReporteOpcion()
            } else {
                Utilities.loadMessageError(
                    reporteOpcionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
    }

    private fun loadDataBase() {
        val rows = myDataBase.getPorIdUser(com.venturessoft.human.utils.User.idUsuario.toString())
        while (rows.moveToNext()) {
            languageOfReport = rows.getString(5)
        }
    }

    private fun loadSuccesReport() {
        val list = java.util.ArrayList<SearchModel>()
        if (FilterReportFragment.empleado != null) {
            for (i in 0 until FilterReportFragment.empleado!![0].checada!!.size) {
                val imageModel = SearchModel()
                val imageModel1 = SearchModel()
                imageModel.setFechaa(formatDate(FilterReportFragment.empleado!![0].checada!![i].fecha))
                imageModel1.setFechaa(formatDate(FilterReportFragment.empleado!![0].checada!![i].fecha))
                val numChecadas: Int = FilterReportFragment.empleado!![0].checada!![i].registro!!.size
                Log.d("ReporteChecadas", "nTotalChecadas: $numChecadas")
                if (numChecadas > 1) {
                    val checadaPar = (numChecadas / 2)
                    Log.d("ReporteChecadas", "checadaPar: $checadaPar")
                    var indiceES = 0
                    for (j in 0 until checadaPar) {
                        val imageModel1 = SearchModel()
                        imageModel1.setFechaa(formatDate(FilterReportFragment.empleado!![0].checada!![i].fecha))
                        imageModel1.setHoraEntradaa(FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES].hora)
                        imageModel1.setHoraSalidaa(FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES + 1].hora)
                        if (FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES].estacion == FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES + 1].estacion) {
                            imageModel1.setNameStationn(FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES].estacion)
                        } else {
                            val station2: String = FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES].estacion + " - " + FilterReportFragment.empleado!![0].checada!![i].registro!![indiceES + 1].estacion
                            imageModel1.setNameStationn(station2)
                        }
                        list.add(imageModel1)
                        indiceES += 2
                    }
                    //Verifica si el numero es impar y si lo es toma el ultimo elemento y muestra en la tabla
                if (numImpar(numChecadas)){
                    try {
                        val indice = numChecadas-1
                        val imageModel1 = SearchModel()
                        imageModel1.setFechaa(formatDate(FilterReportFragment.empleado!![0].checada!![i].fecha))
                        imageModel1.setHoraEntradaa(FilterReportFragment.empleado!![0].checada!![i].registro!![indice].hora)
                        imageModel1.setNameStationn(FilterReportFragment.empleado!![0].checada!![i].registro!![indice].estacion)
                        list.add(imageModel1)
                    }catch (e:Exception){
                        println(e)
                    }
                }


                } else {
                    val imageModel1 = SearchModel()
                    if (FilterReportFragment.empleado != null){
                        try {
                            imageModel1.setFechaa(formatDate(FilterReportFragment.empleado!![0].checada!![i].fecha))
                            imageModel1.setHoraEntradaa(FilterReportFragment.empleado!![0].checada!![i].registro!![0].hora)
                            imageModel1.setNameStationn(FilterReportFragment.empleado!![0].checada!![i].registro!![0].estacion)
                            list.add(imageModel1)
                        }catch (e:Exception){
                            println(e)
                        }

                    }
                }
            }

        }
        val itemDecorator = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider_recicler)!!)
        binding.employeereportrecicler.addItemDecoration(itemDecorator)
        imageModelArrayList = list
        simpleAdapter = AdapterReportEmployee(requireActivity().applicationContext!!, imageModelArrayList)
        binding.employeereportrecicler.adapter = simpleAdapter
        binding.employeereportrecicler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity().applicationContext)
        // Inflate the layout for this fragment
        binding.employeereportrecicler.addOnItemTouchListener(
                RecyclerTouchListener(
                        requireActivity().applicationContext,
                    binding.employeereportrecicler,
                        object : ClickListener {
                            override fun onClick(view: View, position: Int) {

                            }

                            override fun onLongClick(view: View?, position: Int) {

                            }
                        })
        )
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                simpleAdapter.removeAt(viewHolder.adapterPosition)
                // println("REMOVE" + viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.employeereportrecicler)

    }

    private fun numImpar(num:Int):Boolean {

        return num % 2 != 0
    }

    private fun formatDate(date: String): String {
        var newDate = ""
        newDate = date.substring(date.length - 2, date.length) + "/" + date.substring(date.length - 5, date.length - 3) + "/" + date.substring(0, 4)

        return newDate
    }

    private fun loadDataDefault() {
        when (com.venturessoft.human.utils.User.ambiente) {
            "HU" -> {
                if (arguments != null) {
                    numEmpleado = requireArguments().getString("numEmployee")!!
                    dateFrom = requireArguments().getString("dateFrom")!!
                    dateUp = requireArguments().getString("dateUp")!!
                    //  println("Numero de empleado: " + arguments!!.getString("numEmployee"))
                    //println("Date From: " + arguments!!.getString("dateFrom"))
                    // println("Date Up: " + arguments!!.getString("dateUp"))
                    val request = ReporteChecadasRequest(dateFrom, dateUp, numEmpleado.toLong(), com.venturessoft.human.utils.User.ciaVinculado, idUsuario = com.venturessoft.human.utils.User.idUsuario)
                    val requestJson = Gson()
                    var json = requestJson.toJson(request)
                    // println("JSON Entradao : " + json)
                    reportByEmployeeViewModel.getReporteChecadaService(request)
                    println("Carga datos ReporByEmployee Vinculado: ")
                }
            }

            "ET"->{
                if (arguments != null) {
                    numEmpleado = requireArguments().getString("numEmployee")!!
                    dateFrom = requireArguments().getString("dateFrom")!!
                    dateUp = requireArguments().getString("dateUp")!!
                    //  println("Numero de empleado: " + arguments!!.getString("numEmployee"))
                    //println("Date From: " + arguments!!.getString("dateFrom"))
                    // println("Date Up: " + arguments!!.getString("dateUp"))
                    val request = ReporteChecadasRequest(dateFrom, dateUp, numEmpleado.toLong(), com.venturessoft.human.utils.User.scia!![0].cia, idUsuario = com.venturessoft.human.utils.User.idUsuario)
                    val requestJson = Gson()
                    var json = requestJson.toJson(request)
                    // println("JSON Entradao : " + json)
                    reportByEmployeeViewModel.getReporteChecadaService(request)
                    println("Carga datos ReporByEmployee Desvinculado: ")
                }
            }}


    }

    private fun loadReciclerData() {
        binding.employeereportrecicler.adapter = simpleAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = Preferences.getUser(requireActivity().applicationContext)
        loadNameUser()


        mListener?.onBack("backMenuReportByEmp")
        //Email Report
        when (com.venturessoft.human.utils.User.ambiente) {
            "HU" -> {
                binding.emailReportEmployee.setOnClickListener {
                    typeOfReport = "Email"
                    val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, numeroEmpleado = numEmpleado.toInt(), idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.ciaVinculado,
                        fechaInicio = dateFrom, fechaFin = dateUp, opcion = typeOfReport, idioma = getString(R.string.languague).toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.ciaVinculado)
                    reportByEmployeeViewModel.getReporteEFileService(request)
                    Log.i("ReportByEmployee", "El reporte de opcion es: " + Gson().toJson(request))
                }
                binding.pdfReportEmployee.setOnClickListener {
                    typeOfReport = "Pdf"
                    val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, numeroEmpleado = numEmpleado.toInt(), idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.ciaVinculado ,
                        fechaInicio = dateFrom, fechaFin = dateUp, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.ciaVinculado)
                    reportByEmployeeViewModel.getReporteEFileService(request)
                    Log.i("ReportByEmployee", "El reporte de opcion PDF es: " + Gson().toJson(request))
                }
                binding.excelReportEmployee.setOnClickListener {
                    typeOfReport = "Csv"
                    val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, numeroEmpleado = numEmpleado.toInt(), idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.ciaVinculado,
                        fechaInicio = dateFrom, fechaFin = dateUp, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.ciaVinculado)
                    reportByEmployeeViewModel.getReporteEFileService(request)
                    Log.i("ReportByEmployee", "El reporte de opcion CSV es: " + Gson().toJson(request))
                }
            }
            "ET"->{
                binding.emailReportEmployee.setOnClickListener {
                    typeOfReport = "Email"
                    val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, numeroEmpleado = numEmpleado.toInt(), idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.scia!![0].cia ,
                        fechaInicio = dateFrom, fechaFin = dateUp, opcion = typeOfReport, idioma = getString(R.string.languague).toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia, )
                    reportByEmployeeViewModel.getReporteEFileService(request)
                    Log.i("ReportByEmployee", "El reporte de opcion es: " + Gson().toJson(request))
                }
                binding.pdfReportEmployee.setOnClickListener {
                    typeOfReport = "Pdf"
                    val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, numeroEmpleado = numEmpleado.toInt(), idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.scia!![0].cia,
                        fechaInicio = dateFrom, fechaFin = dateUp, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia)
                    reportByEmployeeViewModel.getReporteEFileService(request)
                    Log.i("ReportByEmployee", "El reporte de opcion PDF es: " + Gson().toJson(request))
                }
                binding.excelReportEmployee.setOnClickListener {
                    typeOfReport = "Csv"
                    val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, numeroEmpleado = numEmpleado.toInt(), idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.scia!![0].cia,
                        fechaInicio = dateFrom, fechaFin = dateUp, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia, )
                    reportByEmployeeViewModel.getReporteEFileService(request)
                    Log.i("ReportByEmployee", "El reporte de opcion CSV es: " + Gson().toJson(request))
                }
            }}



        binding.btnBackFilter.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, FilterReportFragment(), "FilterReport")
                    .commit()
        }

    }

    private fun loadNameUser() {
        if (arguments != null) {
            nEmployee = requireArguments().getString("numEmployee")
            nameEmployee = requireArguments().getString("nameEmploye")
            apPaterno = requireArguments().getString("apPaterno")
            if (!requireArguments().getString("apMaterno").isNullOrEmpty()) {
                apMaterno = requireArguments().getString("apMaterno")
            }
        }
        binding.numEmployeeText.text = nEmployee
        binding.nameEmployeeText.text = apPaterno +
                " " + apMaterno + " " +
                nameEmployee

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    fun showLoadingAnimation() {
        binding.loadingAnimotionReport.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimotionReport.visibility = View.GONE
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

    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }

    internal class RecyclerTouchListener(
            context: Context,
            recyclerView: RecyclerView,
            private val clickListener: ClickListener?
    ) : RecyclerView.OnItemTouchListener {
        private val gestureDetector: GestureDetector

        init {
            gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                    }
                }
            })
        }

        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            val child = rv.findChildViewUnder(e.x, e.y)
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child))
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

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
}
