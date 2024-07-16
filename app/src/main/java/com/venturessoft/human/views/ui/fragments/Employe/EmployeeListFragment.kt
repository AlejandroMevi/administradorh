package com.venturessoft.human.views.ui.fragments.Employe

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentEmployeeListBinding
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.request.ActualizaEmpleadoRequest
import com.venturessoft.human.models.request.BusquedaEmpleadoRequest
import com.venturessoft.human.models.request.ConsultaEmpleadoRequest
import com.venturessoft.human.models.request.EliminaEmpleadoRequest
import com.venturessoft.human.models.response.ActualizaEmpleadoResponse
import com.venturessoft.human.models.response.BusquedaEmpleadoResponse
import com.venturessoft.human.models.response.ConsultaEmpleadoResponse
import com.venturessoft.human.models.response.EliminaEmpleadoResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.SimpleAdapterEmployee
import com.venturessoft.human.views.ui.activities.SwipeToDeleteCallback
import com.venturessoft.human.views.ui.fragments.welcome.UpdateEnterpriceFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.EmployeeMaintenanceFragmentViewModel

class EmployeeListFragment : androidx.fragment.app.Fragment(){
    private var employeeMaintenanceFragmentViewModel = EmployeeMaintenanceFragmentViewModel()
    private var busquedaEmpleadoResponse = BusquedaEmpleadoResponse()
    private var busquedaEmpleadoNumResponse = ConsultaEmpleadoResponse()
    private var actualizaEmpleadoResponse = ActualizaEmpleadoResponse()
    private var eliminaEmpleadoResponse = EliminaEmpleadoResponse()
    private var mListener: OnFragmentInteractionListener? = null
    private var adapter: SimpleAdapterEmployee? = null
    private var mainInterface: MainInterface? = null
    private var filtroEmpleado: String = ""
    private var pagina: Int = 1
    var numRegistros: Int = 500

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    companion object {
        var status: String? = null
        var respuestaEliminaEmpleado: String = ""
        lateinit var myContext: Activity
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var idSelected = ""
        var numEmployeRegister: String = ""
        var foto: String? = null
        var correo: String? = null
        var nameEmployee: String = ""
        var apPaternoEmployee: String = ""
        var apMaternoEmployee: String = ""
        var estaciones: ArrayList<Int>? = null
        var zonas: ArrayList<Int>? = null
        var descripcionZona: String = ""
    }

    private lateinit var binding: FragmentEmployeeListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEmployeeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backButonMaintenanceMethod")
        listenButtons()
        loadServices()
        addObservers()
        listenerbtnEditEmploye()
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

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.list_employee_title))
    }
    private fun loadServices() {
        val request = BusquedaEmpleadoRequest(
            idCia = User.scia!![0].cia,
            filtroEmpleado = filtroEmpleado,
            pagina = pagina,
            numRegistros = numRegistros,
            idUsuario = User.idUsuario
        )
        employeeMaintenanceFragmentViewModel.getBusquedaEmpleadoService(request)
    }

    private fun addObservers() {
        employeeMaintenanceFragmentViewModel.busquedaEmpleadoResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            busquedaEmpleadoResponse = it
            if (busquedaEmpleadoResponse.codigo == "ET000") {
                successConsultaEmpleado()
                freeTrail()
            } else if (busquedaEmpleadoResponse.codigo != "ET327") {
                Utilities.loadMessageError(
                    busquedaEmpleadoResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            } else {
                if (adapter != null) {
                    adapter!!.refreshRecicler()
                }
            }
        }

        employeeMaintenanceFragmentViewModel.busquedaEmpleadoResponseMutableData.observe(viewLifecycleOwner) {
            busquedaEmpleadoResponse = it
            if (busquedaEmpleadoResponse.codigo == "ET000") {
            } else if (busquedaEmpleadoResponse.codigo == "ET327") {
                Toast.makeText(activity, "No hay ningun empleado con la letra seleccionada", Toast.LENGTH_SHORT).show()
            } else {
                if (adapter != null) {
                    adapter!!.refreshRecicler()
                }
            }
        }
        employeeMaintenanceFragmentViewModel.busquedaEmpleadoNumResponseMutableData.observe(viewLifecycleOwner) {
            busquedaEmpleadoNumResponse = it
            if (busquedaEmpleadoNumResponse.codigo == "ET000") {
                successConsultaEmpleadoNum()
            } else {
                Utilities.loadMessageError(
                    busquedaEmpleadoNumResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        employeeMaintenanceFragmentViewModel.eliminaEmpleadoResponseMutableData.observe(viewLifecycleOwner) {
            eliminaEmpleadoResponse = it
            if (eliminaEmpleadoResponse.codigo == "ET000") {
                successEliminaEmployee()
            } else {
            }
        }

        employeeMaintenanceFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }

        employeeMaintenanceFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }

        employeeMaintenanceFragmentViewModel.actualizaEmpleadoResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            actualizaEmpleadoResponse = it
            if (actualizaEmpleadoResponse.codigo == "ET000") {
                successUpdateEmployee()
            } else {
                Utilities.loadMessageError(
                    actualizaEmpleadoResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }

        employeeMaintenanceFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }

        employeeMaintenanceFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }

    private fun successConsultaEmpleadoNum() {
        foto = busquedaEmpleadoNumResponse.empleado!![0].foto
        correo = busquedaEmpleadoNumResponse.empleado!![0].correo
        estaciones = busquedaEmpleadoNumResponse.empleado!![0].idEstacion
        zonas = busquedaEmpleadoNumResponse.empleado!![0].idZona
        descripcionZona = busquedaEmpleadoNumResponse.empleado!![0].descripcionZona
        val editEmployeeFragment = EditEmployeeFragment()
        val bundle = Bundle()
        bundle.putString("numEmployeRegister", numEmployeRegister)
        bundle.putString("nameEmployee", nameEmployee)
        bundle.putString("apPaternoEmployee", apPaternoEmployee)
        bundle.putString("apMaternoEmployee", apMaternoEmployee)
        bundle.putString("foto", foto)
        bundle.putString("correo", correo)
        bundle.putString("descripcionZona", descripcionZona)
        bundle.putString("idEstaciones", estaciones.toString())
        editEmployeeFragment.arguments = bundle
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragpricipal, editEmployeeFragment)
            .commit()
    }

    private fun successEliminaEmployee() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            getString(R.string.ok),
            null,
            {
                respuestaEliminaEmpleado = eliminaEmpleadoResponse.codigo
                if (adapter != null) {
                    adapter!!.refreshRecicler()
                    loadServices()
                }
            }
        )
        dialogLogout.show(childFragmentManager,"")
    }

    private fun successUpdateEmployee() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            getString(R.string.ok),
            null,
            {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, EmployeeListFragment(), "WelcomeFragment")
                    .commit()
            }
        )
        dialogLogout.show(childFragmentManager,"")
    }

    private fun successConsultaEmpleado() {
        loadReciclerEmployes()
    }

    private fun loadReciclerEmployes() {
        imageModelArrayList = populateList()
        adapter = SimpleAdapterEmployee(requireActivity().applicationContext!!, imageModelArrayList)
        binding.reciclerMaintenanceActive.adapter = adapter
        binding.reciclerMaintenanceActive.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity().applicationContext,
                binding.reciclerMaintenanceActive,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                    }

                    override fun onLongClick(view: View?, position: Int) {

                    }
                })
        )
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter!!.removeAtView(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.reciclerMaintenanceActive)
    }

    private fun freeTrail() {
        if (busquedaEmpleadoResponse.totalRegistros >= 10) {
            binding.linearFreeTrial.cardFree.isVisible = User.freeTrial
            binding.linearFreeTrial.txtFreeTrial.text = getString(R.string.free_trail_empleados)
            binding.linearFreeTrial.actualizarAhora.setOnClickListener {
                val modalBottomSheet = UpdateEnterpriceFragment()
                modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
            }
        }
    }

    private fun registerEmploye() {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.Fragpricipal, RegisterEmployeeFragment(), "WelcomeFragment")
            .commit()
    }

    private fun listenButtons() {
        binding.btnBackMaintenance.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                .commit()
        }

        binding.addEmployee.setOnClickListener {
            if (User.freeTrial) {
                if (busquedaEmpleadoResponse.totalRegistros == 0 || busquedaEmpleadoResponse.empleado.isNullOrEmpty()) {
                    registerEmploye()
                } else {
                    if (busquedaEmpleadoResponse.totalRegistros >= 10) {
                        val modalBottomSheet = UpdateEnterpriceFragment()
                        modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
                    } else {
                        registerEmploye()
                    }
                }
            } else {
                registerEmploye()
            }
        }

        binding.deleteEmployee.setOnClickListener {
            val request = EliminaEmpleadoRequest(
                idCia = User.scia!![0].cia,
                idEmpleado = idSelected.toLong(),
                idUsuario = User.idUsuario
            )
            employeeMaintenanceFragmentViewModel.getEliminaEmpleadoService(request)
        }

        binding.btnEditStatusEmployee.setOnClickListener {
            val request = ActualizaEmpleadoRequest(estatus = status)
            employeeMaintenanceFragmentViewModel.putActualizaEmpleadoServices(request, idCia = User.scia!![0].cia,
                idEmpleado = idSelected.toLong(),idUsuario = User.idUsuario)
        }
        binding.etFilter.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrBlank()) {
                val request = BusquedaEmpleadoRequest(
                    idCia = User.scia!![0].cia,
                    filtroEmpleado = filtroEmpleado,
                    pagina = pagina,
                    numRegistros = numRegistros,
                    idUsuario = User.idUsuario
                )
                employeeMaintenanceFragmentViewModel.getBusquedaEmpleadoService(request)

            } else {
                val request = BusquedaEmpleadoRequest(
                    idCia = User.scia!![0].cia,
                    filtroEmpleado = text.toString(),
                    pagina = pagina,
                    numRegistros = numRegistros,
                    idUsuario = User.idUsuario
                )
                employeeMaintenanceFragmentViewModel.getBusquedaEmpleadoService(request)
            }
        }
    }
    fun eliminarEmpleado(id: String) {
        idSelected = id
        val btnDelete = myContext.findViewById<Button>(R.id.deleteEmployee)
        btnDelete.callOnClick()
    }
    private fun listenerbtnEditEmploye() {
        binding.btnGoToEditEmployee.setOnClickListener {
            val request = ConsultaEmpleadoRequest(
                idCia = User.scia!![0].cia,
                idEmpleado = numEmployeRegister.toLong(),
                idUsuario = User.idUsuario
            )

            employeeMaintenanceFragmentViewModel.getBusquedaEmpleadoNumService(request)
        }
    }

    fun updateMaintenance(id: String, estatuss: String) {
        idSelected = id
        status = if (estatuss == "A") {
            "B"
        } else {
            "A"
        }
        val btnEditStatus = myContext.findViewById<Button>(R.id.btnEditStatusEmployee)
        btnEditStatus.callOnClick()

    }

    fun editEmployee(
        id: String,
        numberItem: String,
        nameItem: String,
        apPaternoItem: String,
        apMaternoItem: String,
        fotoo: String
    ) {
        idSelected = id
        numEmployeRegister = numberItem
        nameEmployee = nameItem
        apPaternoEmployee = apPaternoItem
        apMaternoEmployee = apMaternoItem
        val btnEditSuoer = myContext.findViewById<Button>(R.id.btnGoToEditEmployee)
        idSelected = id
        btnEditSuoer.callOnClick()
    }

    private fun populateList(): java.util.ArrayList<SearchModel> {
        val list = java.util.ArrayList<SearchModel>()
        for (i in 0 until busquedaEmpleadoResponse.totalRegistros) {
            val imageModel = SearchModel()
            imageModel.setNames(busquedaEmpleadoResponse.empleado?.get(i)?.nombre.toString())
            imageModel.setNumberr(busquedaEmpleadoResponse.empleado?.get(i)?.idEmpleado.toString())
            imageModel.setApPaternoo(busquedaEmpleadoResponse.empleado?.get(i)?.apellidoPat.toString())
            if (busquedaEmpleadoResponse.empleado?.get(i)?.apellidoMat != null) {
                imageModel.setApMaternoo(busquedaEmpleadoResponse.empleado?.get(i)?.apellidoMat.toString())
            } else {
                imageModel.setApMaternoo("")
            }
            imageModel.setStatuss(busquedaEmpleadoResponse.empleado?.get(i)?.estatus.toString())
            imageModel.setIdd(busquedaEmpleadoResponse.empleado?.get(i)?.idEmpleado.toString())
            imageModel.setFotoo(busquedaEmpleadoResponse.empleado?.get(i)?.foto.toString())
            list.add(imageModel)
        }
        return list
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
            gestureDetector =
                GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
                    override fun onSingleTapUp(e: MotionEvent): Boolean {
                        return true
                    }

                    override fun onLongPress(e: MotionEvent) {
                        val child = recyclerView.findChildViewUnder(e.x, e.y)
                        if (child != null && clickListener != null) {
                            clickListener.onLongClick(child, recyclerView.getChildPosition(child))
                        }
                    }
                }
                )
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

    fun showLoadingAnimation() { binding.loadingAnimationListEmployee.root.isVisible = true }

    fun hideLoadingAnimation() { binding.loadingAnimationListEmployee.root.isVisible = false }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val indentificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (adapter != null) {
            adapter!!.refreshRecicler()
        }
        val dialogLogout = DialogGeneral(
            getString(R.string.error),
            getString(indentificadorMensaje),
            getString(R.string.ok)
        )
        dialogLogout.show(childFragmentManager,"")
    }
}
