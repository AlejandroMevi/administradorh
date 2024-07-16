package com.venturessoft.human.views.ui.fragments.Stations

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentStationsListBinding
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.request.ActualizaEstacionRequest
import com.venturessoft.human.models.request.BusquedaEstacionRequest
import com.venturessoft.human.models.request.BusquedaZonaRequest
import com.venturessoft.human.models.request.ConsultaEstacionRequest
import com.venturessoft.human.models.request.EliminaEstacionRequest
import com.venturessoft.human.models.response.ActualizaEstacionResponse
import com.venturessoft.human.models.response.BusquedaEstacionResponse
import com.venturessoft.human.models.response.BusquedaZonaResponse
import com.venturessoft.human.models.response.ConsultaEstacionResponse
import com.venturessoft.human.models.response.EliminaEstacionResponse
import com.venturessoft.human.models.response.sZonas
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.adapters.SimpleAdapterEstaciones
import com.venturessoft.human.views.ui.activities.SwipeToDeleteCallback
import com.venturessoft.human.views.ui.fragments.welcome.UpdateEnterpriceFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.ListEstacionesFragmentViewModel
class ListEstacionesFragment : androidx.fragment.app.Fragment(){
    private var mListener: OnFragmentInteractionListener? = null
    private var listEstacionesFragmentViewModel = ListEstacionesFragmentViewModel()
    private var busquedaEstacionResponse = BusquedaEstacionResponse()
    private var busquedaZonasResponse = BusquedaZonaResponse()
    private var consultaEstacionUnoResponse = ConsultaEstacionResponse()
    private var actualizaEstacionResponse = ActualizaEstacionResponse()
    private var eliminaEstacionResponse = EliminaEstacionResponse()
    private var mainInterface: MainInterface? = null
    var adapter: SimpleAdapterEstaciones? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
        var listZones: ArrayList<sZonas>? = null
        var idStation: Int? = null
        var estatus: String? = null
        var deleteResultStation: String = ""
        lateinit var myContext: Activity
        lateinit var myDataBase: DBDemo
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var idSelected: Int? = null
    }
    private lateinit var binding: FragmentStationsListBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentStationsListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backListEstacion")
        listensButtos()
        addObserver()
        val request = BusquedaEstacionRequest(User.idUsuario, "")
        listEstacionesFragmentViewModel.getBusquedaEstacion(request)
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.list_estaciones_title))
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
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
    private fun freeTrail() {
        if (busquedaEstacionResponse.estacion!!.size >= 5) {
            binding.linearFreeTrial.cardFree.isVisible = User.freeTrial
            binding.linearFreeTrial.txtFreeTrial.text = getString(R.string.free_trail_estaciones)
            binding.linearFreeTrial.actualizarAhora.setOnClickListener {
                val modalBottomSheet = UpdateEnterpriceFragment()
                modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
            }
        }
    }
    private fun stationsFragments() {
        when (User.ambiente) {
            "ET" -> {
                if (User.tipoUsuario == 2 && User.estaciones == true) {

                } else {
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.Fragpricipal, StationsFragment(), "WelcomeFragment").commit()
                }
            }
        }
    }
    private fun listensButtos() {
        binding.addEstacion.setOnClickListener {
            if (User.freeTrial) {
                if (busquedaEstacionResponse.estacion?.isEmpty() == true ) {
                    stationsFragments()
                } else {
                    if (busquedaEstacionResponse.estacion!!.size >= 5) {
                        val modalBottomSheet = UpdateEnterpriceFragment()
                        modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
                    } else {
                        stationsFragments()
                    }
                }
            } else {
                stationsFragments()
            }
        }
        binding.btnBackMenuListEst.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                .commit()
        }
        binding.btnGoToEdit.setOnClickListener {
            EditStationsFragment.idStation = idSelected
            val requestConsultaEstacion = ConsultaEstacionRequest(User.idUsuario, idSelected)
            listEstacionesFragmentViewModel.getConsultaEstacionUno(requestConsultaEstacion)
        }
        binding.btnEditStatus.setOnClickListener {
            val request = ActualizaEstacionRequest(
                idUsuario = User.idUsuario,
                idEstacion = idStation!!,
                status = estatus!!
            )
            listEstacionesFragmentViewModel.getActualizaEstacionService(request)
        }
        binding.btnDeleteStation.setOnClickListener {
            val request = EliminaEstacionRequest(User.idUsuario, idStation!!)
            listEstacionesFragmentViewModel.getEliminaEstacion(request)
        }
        binding.etFilter.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                val request = BusquedaEstacionRequest(User.idUsuario, "")
                listEstacionesFragmentViewModel.getBusquedaEstacion(request)
            } else {
                val request = BusquedaEstacionRequest(User.idUsuario, text.toString())
                listEstacionesFragmentViewModel.getBusquedaEstacion(request)
            }
        }
    }
    private fun addObserver() {
        listEstacionesFragmentViewModel.busquedaEstacionResponseMutableData.observe(viewLifecycleOwner) {
            busquedaEstacionResponse = it
            if (busquedaEstacionResponse.codigo == "ET000") {
                if (busquedaEstacionResponse.estacion!!.size > 0) {
                    loadReciclerView()
                    freeTrail()
                }
            } else if (busquedaEstacionResponse.codigo != "ET327") {
                Utilities.loadMessageError(
                    busquedaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            } else {
                if (adapter != null) {
                    adapter!!.refreshStations()
                }
            }
        }
        listEstacionesFragmentViewModel.consultaEstacionUnoResponseMutableData.observe(viewLifecycleOwner) {
            consultaEstacionUnoResponse = it
            if (consultaEstacionUnoResponse.codigo == "ET000") {
                loadServicesZones()
            } else {
                Utilities.loadMessageError(
                    consultaEstacionUnoResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        listEstacionesFragmentViewModel.busquedaZonaResponseMutableData.observe(viewLifecycleOwner) {
            busquedaZonasResponse = it
            if (busquedaZonasResponse.codigo == "ET000") {
                loadDataNextView()
            } else {
                Utilities.loadMessageError(
                    busquedaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        listEstacionesFragmentViewModel.actualizaEstacionResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            actualizaEstacionResponse = it
            if (actualizaEstacionResponse.codigo == "ET000") {
                successUpdateStatus()
            } else {
                Utilities.loadMessageError(
                    actualizaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        listEstacionesFragmentViewModel.eliminaEstacionResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            eliminaEstacionResponse = it
            if (eliminaEstacionResponse.codigo == "ET000") {
                deleteResultStation = eliminaEstacionResponse.codigo
                successDeleteStation()

            } else {
                Utilities.loadMessageError(
                    eliminaEstacionResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        listEstacionesFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        listEstacionesFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }
    private fun loadServicesZones() {
        val request = BusquedaZonaRequest(User.idUsuario)
        listEstacionesFragmentViewModel.getBusquedaZonas(request)
    }
    private fun loadDataNextView() {
        val editStationsFragment = EditStationsFragment()
        val contentData = Bundle()
        contentData.putString("nameEstationEdit", consultaEstacionUnoResponse.sEstacion!![0].nombre)
        contentData.putString("idZona", consultaEstacionUnoResponse.sEstacion!![0].idZona.toString())
        contentData.putDouble("latitud", consultaEstacionUnoResponse.sEstacion!![0].latitud)
        contentData.putDouble("longitud", consultaEstacionUnoResponse.sEstacion!![0].longitud)
        contentData.putFloat("rango", consultaEstacionUnoResponse.sEstacion!![0].rango)
        contentData.putString("uuid", consultaEstacionUnoResponse.sEstacion!![0].uuid)
        contentData.putString("bssid", consultaEstacionUnoResponse.sEstacion!![0].bssid)
        contentData.putString("clvUnidad", consultaEstacionUnoResponse.sEstacion!![0].clvUnidad)
        contentData.putString("status", consultaEstacionUnoResponse.sEstacion!![0].status)
        contentData.putBoolean("estacionLibre", consultaEstacionUnoResponse.sEstacion!![0].estacionLibre)
        listZones = busquedaZonasResponse.sZona
        editStationsFragment.arguments = contentData
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragpricipal, editStationsFragment, "WelcomeFragment")
            .commit()
    }
    private fun loadReciclerView() {
        val list = java.util.ArrayList<SearchModel>()
        for (i in 0 until busquedaEstacionResponse.estacion!!.size) {
            val dataModel = SearchModel()
            dataModel.setNames(busquedaEstacionResponse.estacion!![i].nombre)
            dataModel.setStatuss(if (busquedaEstacionResponse.estacion!![i].status == null) "" else busquedaEstacionResponse.estacion!![i].status)
            dataModel.setIdd(busquedaEstacionResponse.estacion!![i].idEstacion.toString())
            list.add(dataModel)
        }
        imageModelArrayList = list
        adapter = SimpleAdapterEstaciones(requireActivity().applicationContext, imageModelArrayList)
        binding.reciclerEstaciones.adapter = adapter
        binding.reciclerEstaciones.addOnItemTouchListener(
            RecyclerTouchListener(requireActivity().applicationContext, binding.reciclerEstaciones, object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                    }
                    override fun onLongClick(view: View?, position: Int) {
                    }
                }
            )
        )
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter!!.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.reciclerEstaciones)
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
    fun eliminarEstacion(id: String) {
        idStation = id.toInt()
        val btnDeleteEstacion = myContext.findViewById<Button>(R.id.btnDeleteStation)
        btnDeleteEstacion.callOnClick()
    }
    fun updateEstacion(id: Int, status: String) {
        var newStatus = ""
        when (status) {
            "A" -> newStatus = "B"
            "B" -> newStatus = "A"
        }
        idStation = id
        estatus = newStatus
        val btnEditEstacion = myContext.findViewById<Button>(R.id.btnEditStatus)
        btnEditEstacion.callOnClick()
    }
    fun editEstacion(id: Int) {
        val btnEditEstacion = myContext.findViewById<Button>(R.id.btnGoToEdit)
        idSelected = id
        btnEditEstacion.callOnClick()
    }
    fun showLoadingAnimation() {
        binding.loadingAnimationStation.root.isVisible = true
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }
    fun hideLoadingAnimation() {
        binding.loadingAnimationStation.root.isVisible = false
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
    private fun successUpdateStatus() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            null,
            null,
            {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, ListEstacionesFragment(), "WelcomeFragment")
                    .commit()
            }
        )
        dialogLogout.show(childFragmentManager,"")
    }
    private fun successDeleteStation() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            null,
            null,
            {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, ListEstacionesFragment(), "WelcomeFragment")
                    .commit()
            }
        )
        dialogLogout.show(childFragmentManager,"")
    }
    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje = this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            val dialogLogout = DialogGeneral(
                getString(R.string.error),
                getString(identificadorMensaje),
                null,
                null,
                {
                    if (adapter != null) {
                        adapter!!.refreshStations()
                    }
                }
            )
            dialogLogout.show(childFragmentManager,"")
        }
    }
}
