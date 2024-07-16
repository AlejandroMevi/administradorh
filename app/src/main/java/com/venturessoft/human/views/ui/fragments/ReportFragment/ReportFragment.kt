package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentReportBinding
import com.venturessoft.human.models.request.ReporteChecadasRequest
import com.venturessoft.human.models.request.ReporteOpcionRequest
import com.venturessoft.human.models.response.ReporteChecadasResponse
import com.venturessoft.human.models.response.ReporteOpcionResponse
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.utils.OnSwipeTouchListener
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.SimpleAdapterReport
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.activities.SwipeToDeleteCallback
import com.venturessoft.human.views.ui.fragments.Filter.FilterReportFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.ReportFragmentViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ReportFragment : androidx.fragment.app.Fragment(), View.OnClickListener {
    private lateinit var binding : FragmentReportBinding
    private var mListener: OnFragmentInteractionListener? = null
    private var reporteOpcionResponse = ReporteOpcionResponse()
    private var counter = 0
    lateinit var simpleAdapter: SimpleAdapterReport
    private var reporteChecadasResponse = ReporteChecadasResponse()
    private var reportFragmentViewModel = ReportFragmentViewModel()
    private var inActiveDate: String? = null
    private var inActiveDateFormat: String? = null
    private var activeFiltro = false
    private var dateFilter = Date()
    private var typeOfReport: String = ""
    private var languageOfReport: String = "EN"
    private var dateFromSend ="yyyy-mm-dd"
    private var dateUpSend ="yyyy-mm-dd"

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        lateinit var myDataBase: BDPayments
        lateinit var imageModelArrayList: ArrayList<SearchModel>
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)

        binding.reportrecicler.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(requireActivity(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        binding.reportrecicler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity())
        loadObservers()



        return binding.root
    }

    private fun loadServices() {
        //  println("Dia Inactivo: " + inActiveDateFormat)
        val reporteChecadaRequest = ReporteChecadasRequest(idCia = com.venturessoft.human.utils.User.scia!![0].cia,
                fechaInicio = inActiveDateFormat!!, fechaFin = inActiveDateFormat!!)
        reportFragmentViewModel.getReporteChecadaService(reporteChecadaRequest)
    }

    private fun loadObservers() {
        reportFragmentViewModel.reporteChecadasResponseMutableData.observe(viewLifecycleOwner
        ) {
            reporteChecadasResponse = it
            //    println("El reporte de checadas: " + reporteChecadasResponse.codigo)
            when (reporteChecadasResponse.codigo) {
                "ET000" -> {
                    successReporteChecada()
                }
                "ET327" -> {
                    successReporteChecada()
                    simpleAdapter.deleteAllData()
                    val contextoPaquete: String = requireActivity().packageName
                    val identificadorMensaje = this.resources.getIdentifier(
                        reporteChecadasResponse.codigo,
                        "string",
                        contextoPaquete
                    )
                    Toast.makeText(requireActivity(), getString(identificadorMensaje), Toast.LENGTH_SHORT)
                        .show()

                }
                else -> {
                    Utilities.showDialog(reporteChecadasResponse.codigo, requireActivity(),childFragmentManager)
                }
            }
        }

        reportFragmentViewModel.isLoading.observe(viewLifecycleOwner
        ) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        reportFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }

        reportFragmentViewModel.reporteopcionResponseMutableData.observe(viewLifecycleOwner) {
            reporteOpcionResponse = it
            Log.i("ResponsetEmal", "Response: " + it.codigo)

            if (reporteOpcionResponse.codigo == "ET000") {
                successReporteOpcion()
            } else {
            }
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

    private fun successReporteChecada() {
        //Validar si esto no es nulo
        val list = ArrayList<SearchModel>()
        if (reporteChecadasResponse.empleado != null) {
            //Recorre Los Empleados
            for (n_Empleados in 0 until reporteChecadasResponse.empleado!!.size) {

                val imageModel1 = SearchModel()

                //Determina el numero total de checada por empleado
                val numChecadas = reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!!.size
                Log.d("ReporteChecadas", "nTotalChecadas: $numChecadas")

                if (numChecadas > 1) {
                    val checadaPar = (numChecadas / 2)
                    Log.d("ReporteChecadas", "checadaPar: $checadaPar")
                    var indiceES = 0
                    for (j in 0 until checadaPar) {

                       // println("Hora de Entrada : "+indiceES)
                    //    println("Hora de Salida : "+indiceES + 1)
                        if (j % 2 != 0) {
                            val imageModel = SearchModel()
                            imageModel.setNumberr(reporteChecadasResponse.empleado!![n_Empleados].numeroEmpleado!!.toString())
                            imageModel.setNames(reporteChecadasResponse.empleado!![n_Empleados].nombre!!)
                            imageModel.setApPaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoPaterno!!)
                            if (reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno != null) {
                                imageModel.setApMaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno!!)
                            } else {
                                imageModel.setApMaternoo(" ")
                            }

                            imageModel.setHoraEntradaa(reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!![indiceES].hora)
                            imageModel.setHoraSalidaa(reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!![indiceES + 1].hora)
                            list.add(imageModel)
                        }else{
                            val imageModel2 = SearchModel()
                            imageModel2.setNumberr(reporteChecadasResponse.empleado!![n_Empleados].numeroEmpleado!!.toString())
                            imageModel2.setNames(reporteChecadasResponse.empleado!![n_Empleados].nombre!!)
                            imageModel2.setApPaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoPaterno!!)
                            if (reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno != null) {
                                imageModel2.setApMaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno!!)
                            } else {
                                imageModel2.setApMaternoo(" ")
                            }
                            imageModel2.setHoraEntradaa(reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!![indiceES].hora)
                            imageModel2.setHoraSalidaa(reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!![indiceES + 1].hora)
                            list.add(imageModel2)
                        }
                        indiceES += 2

                    }
                    indiceES = 0
                    //Si existe un residuo
                    if (numChecadas % 2 != 0) {
                        imageModel1.setNumberr(reporteChecadasResponse.empleado!![n_Empleados].numeroEmpleado!!.toString())
                        imageModel1.setNames(reporteChecadasResponse.empleado!![n_Empleados].nombre!!)
                        imageModel1.setApPaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoPaterno!!)
                        if (reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno != null) {
                            imageModel1.setApMaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno!!)
                        } else {
                            imageModel1.setApMaternoo(" ")
                        }
                        imageModel1.setHoraEntradaa(reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!![numChecadas-1].hora)
                        list.add(imageModel1)
                    }
                } else {
                    //Entradas
                    val imageModel = SearchModel()
                    imageModel.setNumberr(reporteChecadasResponse.empleado!![n_Empleados].numeroEmpleado!!.toString())
                    imageModel.setNames(reporteChecadasResponse.empleado!![n_Empleados].nombre!!)
                    imageModel.setApPaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoPaterno!!)
                    if (reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno != null) {
                        imageModel.setApMaternoo(reporteChecadasResponse.empleado!![n_Empleados].apellidoMaterno!!)
                    } else {
                        imageModel.setApMaternoo(" ")
                    }
                    imageModel.setHoraEntradaa(reporteChecadasResponse.empleado!![n_Empleados].checada!![0].registro!![0].hora)
                    list.add(imageModel)

                }
            }

        }

        imageModelArrayList = list
        simpleAdapter = SimpleAdapterReport(requireActivity().applicationContext!!, imageModelArrayList)
        binding.reportrecicler.adapter = simpleAdapter
        binding.reportrecicler.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity().applicationContext)
        // Inflate the layout for this fragment
        binding.reportrecicler.addOnItemTouchListener(
                RecyclerTouchListener(
                        requireActivity().applicationContext,
                    binding.reportrecicler,
                        object : ClickListener {
                            override fun onClick(view: View, position: Int) {

                            }

                            override fun onLongClick(view: View?, position: Int) {

                            }
                        })
        )
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                simpleAdapter.removeAt()
                // println("REMOVE" + viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.reportrecicler)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  user = Preferences.getUser(activity!!.applicationContext)
        mListener?.onBack("backMenuReport")
        loadDataBase()
        setTodayDate()
        loadServices()
        loadDataFromFilter()
        binding.substractDayButton.setOnClickListener(this)
        binding.addDayButton.setOnClickListener(this)


        binding.btnAtrasReport.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                    .commit()
        }
        binding.addFilter.setOnClickListener {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, FilterReportFragment(), "WelcomeFragment")
                    .commit()
        }
        //Email Report
        binding.emailReport.setOnClickListener {
            typeOfReport = "Email"
            val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.scia!![0].cia,
                fechaInicio = dateFromSend, fechaFin = dateUpSend, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia)
            reportFragmentViewModel.getReporteFileService(request)
            Log.i("RequestEmal", "Request: " + Gson().toJson(request))
            //   println("Lo que se envia es: "+Gson().toJson(request))
        }
        binding.pdfReport.setOnClickListener {
            typeOfReport = "Pdf"
            val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.scia!![0].cia, fechaInicio = inActiveDateFormat!!,
                fechaFin = inActiveDateFormat!!, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia)
            reportFragmentViewModel.getReporteFileService(request)
        }
        binding.excelReport.setOnClickListener {
            typeOfReport = "Csv"
            val request = ReporteOpcionRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario, idCia = com.venturessoft.human.utils.User.ciaSeleccionada ?: com.venturessoft.human.utils.User.scia!![0].cia, fechaInicio = inActiveDateFormat!!,
                fechaFin = inActiveDateFormat!!, opcion = typeOfReport, idioma = languageOfReport.toUpperCase(), idCiaSupervisor = com.venturessoft.human.utils.User.scia!![0].cia)
            reportFragmentViewModel.getReporteFileService(request)
        }


        binding.reportrecicler.setOnTouchListener(object : OnSwipeTouchListener() {
            override fun onSwipeLeft() {
                binding.addDayButton.performClick()

            }

            override fun onSwipeRight() {
                binding.substractDayButton.performClick()

            }
        })

        if (activeFiltro.not()) {
            disableButtonAndSwipe()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataBase = BDPayments(requireActivity())

    }

    private fun loadDataBase() {
        val rows = myDataBase.getPorIdUser(com.venturessoft.human.utils.User.idUsuario.toString())
        while (rows.moveToNext()) {
            languageOfReport = rows.getString(5)
        }

    }

    private fun loadDataFromFilter() {
        if (arguments != null) {
            activeFiltro = true
            dateFromSend = requireArguments().getString("dateFrom",null)
            dateUpSend = requireArguments().getString("dateUp",null)
            val format1 = SimpleDateFormat("dd/MM/yyyy")
            val date = requireArguments().getString("dateFrom")
            val year = date.toString().substring(0, 4)
            val month = date.toString().substring(5, 7)
            val day = date.toString().substring(8, 10)
            val formatDate = "$day/$month/$year"
            //  println("Formato de dia: "+formatDate)
            val calendar1 = Calendar.getInstance()
            //println("Año: "+year.toInt()+" Mes: "+month.toInt()+" Dia: "+day.toInt())
            dateFilter = SimpleDateFormat("dd/MM/yyyy").parse(formatDate)
            calendar1.time = dateFilter
            inActiveDateFormat = date
            val dayOfTheWeek = resources.getStringArray(R.array.day_of_the_week_array)[calendar1.get(Calendar.DAY_OF_WEEK) - 1/*Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1*/]
            binding.dateTextView.text = "$dayOfTheWeek $inActiveDateFormat"
            val request = ReporteChecadasRequest(fechaInicio = date!!, fechaFin = date, idCia = com.venturessoft.human.utils.User.scia!![0].cia)
            reportFragmentViewModel.getReporteChecadaService(request)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.substractDayButton -> susbtractDay()
            R.id.addDayButton -> addDay()
        }

    }

    private fun setTodayDate() {
        val format1 = SimpleDateFormat("dd/MM/yyyy")
        val format2 = SimpleDateFormat("yyyy-MM-dd")

        try {
            inActiveDate = format1.format(Calendar.getInstance().time)
            inActiveDateFormat = format2.format(Calendar.getInstance().time)
            dateFromSend = format2.format(System.currentTimeMillis())
            dateUpSend = format2.format(System.currentTimeMillis())
            val dayOfTheWeek = resources.getStringArray(R.array.day_of_the_week_array)[Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1]
            binding.dateTextView.text = "$dayOfTheWeek $inActiveDate"
            loadServices()
        } catch (e1: ParseException) { // TODO Auto-generated catch block
            e1.printStackTrace()
        }

    }

    private fun susbtractDay() {
        println("CounterSustrac: $counter")
        if (activeFiltro) {
            counter--
            //    println("El contador tiene: " + counter)
            val calendar = Calendar.getInstance()
            calendar.time = dateFilter

            calendar.add(Calendar.DAY_OF_YEAR, counter)

            if (Date().compareTo(calendar.time) != -1) {
                val format1 = SimpleDateFormat("dd/MM/yyyy")
                val format2 = SimpleDateFormat("yyyy-MM-dd")
                val inActiveDate: String?
                try {
                    inActiveDate = format1.format(calendar.time)
                    inActiveDateFormat = format2.format(calendar.time)
                    dateFromSend = format2.format(calendar.time)
                    dateUpSend = format2.format(calendar.time)
                    val dayOfTheWeek = resources.getStringArray(R.array.day_of_the_week_array)[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                    binding.dateTextView.text = "$dayOfTheWeek $inActiveDate"
                    loadServices()
                } catch (e1: ParseException) {
                    e1.printStackTrace()
                }
            }
            binding.addDayButton.isClickable = true

        } else {
            counter--
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, counter)
            val format1 = SimpleDateFormat("dd/MM/yyyy")
            val format2 = SimpleDateFormat("yyyy-MM-dd")
            val inActiveDate: String?
            try {
                inActiveDate = format1.format(calendar.time)
                inActiveDateFormat = format2.format(calendar.time)
                dateFromSend = format2.format(calendar.time)
                val dayOfTheWeek = resources.getStringArray(R.array.day_of_the_week_array)[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                binding.dateTextView.text = "$dayOfTheWeek $inActiveDate"
                loadServices()
            } catch (e1: ParseException) { // TODO Auto-generated catch block
                e1.printStackTrace()
            }

            disableButtonAndSwipe()
        }
    }

    private fun addDay() {
        if (activeFiltro) {
            //     println("El contador tiene: " + counter)
            val calendar = Calendar.getInstance()
            calendar.time = dateFilter
            counter++
            calendar.add(Calendar.DAY_OF_YEAR, counter)

            if (Date().compareTo(calendar.time) != -1) {
                val format1 = SimpleDateFormat("dd/MM/yyyy")
                val format2 = SimpleDateFormat("yyyy-MM-dd")
                val inActiveDate: String?
                try {
                    inActiveDate = format1.format(calendar.time)
                    inActiveDateFormat = format2.format(calendar.time)
                    dateFromSend = format2.format(calendar.time)
                    dateUpSend = format2.format(calendar.time)
                    //      println("try")
                    val dayOfTheWeek = resources.getStringArray(R.array.day_of_the_week_array)[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                    binding.dateTextView.text = "$dayOfTheWeek $inActiveDate"
                    loadServices()
                } catch (e1: ParseException) {
                    e1.printStackTrace()
                }
            } else {
                //  counter = 0
                binding.addDayButton.isClickable = false
            }
        } else {
            if (counter == 0) {
                return
            }
            counter++
            val calendar = Calendar.getInstance()
            calendar.time = dateFilter

            calendar.add(Calendar.DAY_OF_YEAR, counter)
            val format1 = SimpleDateFormat("dd/MM/yyyy")
            val format2 = SimpleDateFormat("yyyy-MM-dd")
            val inActiveDate: String?
            try {
                inActiveDate = format1.format(calendar.time)
                inActiveDateFormat = format2.format(calendar.time)
                dateFromSend = format2.format(calendar.time)
                //         println("try")
                val dayOfTheWeek = resources.getStringArray(R.array.day_of_the_week_array)[calendar.get(Calendar.DAY_OF_WEEK) - 1]
                binding.dateTextView.text = "$dayOfTheWeek $inActiveDate"
                loadServices()
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }
            disableButtonAndSwipe()
        }
    }

    private fun disableButtonAndSwipe() {
        binding.addDayButton.isClickable = counter != 0
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

    fun showLoadingAnimation() {
        binding.loadingAnimationReport.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationReport.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val indentificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        Utilities.showDialog(requireActivity().getString(indentificadorMensaje), requireActivity(),childFragmentManager)

    }

}
