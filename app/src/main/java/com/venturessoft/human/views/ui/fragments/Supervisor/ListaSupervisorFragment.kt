package com.venturessoft.human.views.ui.fragments.Supervisor

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
import com.venturessoft.human.databinding.FragmentSupervisorsListBinding
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.models.User
import com.venturessoft.human.models.request.ActualizarUsuarioRequest
import com.venturessoft.human.models.request.BusquedaUsuarioRequest
import com.venturessoft.human.models.request.ConsultaUsuarioRequest
import com.venturessoft.human.models.request.EliminaUsuarioRequest
import com.venturessoft.human.models.response.ActualizarUsuarioResponse
import com.venturessoft.human.models.response.BusquedaUsuarioResponse
import com.venturessoft.human.models.response.ConsultaUsuarioResponse
import com.venturessoft.human.models.response.EliminaUsuarioResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.SimpleAdapterSuper
import com.venturessoft.human.views.ui.activities.SwipeToDeleteCallback
import com.venturessoft.human.views.ui.fragments.welcome.UpdateEnterpriceFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.ListSupervisorFragmentViewModel

class ListaSupervisorFragment : androidx.fragment.app.Fragment() {
    private var listaSupervisorFragmentViewModel = ListSupervisorFragmentViewModel()
    private var busquedaUsuarioResponse = BusquedaUsuarioResponse()
    private var consultaUsuarioResponse = ConsultaUsuarioResponse()
    private var eliminaUsuarioResponse = EliminaUsuarioResponse()
    private var actualizaUsuarioResponse = ActualizarUsuarioResponse()
    private var mListener: OnFragmentInteractionListener? = null
    var user: User? = null
    private var adapter: SimpleAdapterSuper? = null
    private var mainInterface: MainInterface? = null

    interface OnFragmentInteractionListener {
        fun onBackMenuLsuper()
    }

    companion object {
        lateinit var myContext: Activity
        var idSelected = ""
        var resultDeleteSuper: String = ""
        var status = ""
        lateinit var imageModelArrayList: ArrayList<SearchModel>
        var nombre: String = ""
        var nameSuper: String = ""
        var apellidoPat: String = ""
        var apellidoMat: String = ""
        var apPaterno: String = ""
        var apMaterno: String = ""
        var correo: String = ""
        var email: String = ""
        var estaciones: Boolean? = false
        var idZonasEliminar: ArrayList<Int> = ArrayList()
    }

    private lateinit var binding: FragmentSupervisorsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myContext = requireActivity()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSupervisorsListBinding.inflate(inflater, container, false)
        addObservers()
        loadServices()
        return binding.root
    }

    private fun freeTrail() {
        if (consultaUsuarioResponse.supervisor!!.size >= 1) {
            binding.linearFreeTrial.cardFree.isVisible = com.venturessoft.human.utils.User.freeTrial
            println("User.freeTrial" + com.venturessoft.human.utils.User.freeTrial)
            binding.linearFreeTrial.txtFreeTrial.text = getString(R.string.free_trail_supervisor)
            binding.linearFreeTrial.actualizarAhora.setOnClickListener {
                val modalBottomSheet = UpdateEnterpriceFragment()
                modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
            }
        }
    }

    private fun addObservers() {
        listaSupervisorFragmentViewModel.busquedaUsuarioResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            busquedaUsuarioResponse = it
            if (busquedaUsuarioResponse.codigo == "ET000") {
                consultaSuccess()
            } else if (busquedaUsuarioResponse.codigo != "ET327") {
                Utilities.loadMessageError(
                    busquedaUsuarioResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            } else {
                if (adapter != null) {
                    adapter!!.refreshRecicler()
                }
            }
        }
        listaSupervisorFragmentViewModel.consultUserResponseMutableData.observe(viewLifecycleOwner) {
            consultaUsuarioResponse = it
            if (consultaUsuarioResponse.codigo == "ET000") {
                loadReciclerSupervisorConsultaU()
                freeTrail()
            } else if (consultaUsuarioResponse.codigo != "ET327") {
                Utilities.loadMessageError(
                    consultaUsuarioResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        listaSupervisorFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        listaSupervisorFragmentViewModel.actualizarUsuarioResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            actualizaUsuarioResponse = it
            if (actualizaUsuarioResponse.codigo == "ET000") {
                successActualizaUsuario()
            } else {
                Utilities.loadMessageError(
                    actualizaUsuarioResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        listaSupervisorFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
        listaSupervisorFragmentViewModel.eliminaUsuarioResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            eliminaUsuarioResponse = it
            if (eliminaUsuarioResponse.codigo == "ET000") {
                resultDeleteSuper = eliminaUsuarioResponse.codigo
                successEliminaUsuario()
            } else {
                Utilities.loadMessageError(
                    eliminaUsuarioResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
    }

    private fun successEliminaUsuario() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            null,
            null,
            {
                adapter!!.refreshRecicler()
                loadServices()
            }
        )
        dialogLogout.show(childFragmentManager, "")
    }

    private fun successActualizaUsuario() {
        val dialogLogout = DialogGeneral(
            getString(R.string.good),
            getString(R.string.successful_change),
            null,
            null,
            {
                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, ListaSupervisorFragment(), "WelcomeFragment")
                    .commit()
            }
        )
        dialogLogout.show(childFragmentManager, "")
    }

    private fun consultaSuccess() {
        if (busquedaUsuarioResponse.usuarioSuper!!.isEmpty()) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.Fragpricipal, RegisterSupervisorFragment(), "WelcomeFragment")
                .commit()
        } else {
            loadReciclerSupervisor()
        }
    }

    private fun loadReciclerSupervisor() {
        imageModelArrayList = listSuper()
        adapter = SimpleAdapterSuper(requireActivity().applicationContext!!, imageModelArrayList)
        binding.reciclerListSuper.adapter = adapter!!
        binding.reciclerListSuper.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity().applicationContext,
                binding.reciclerListSuper,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        nameSuper = imageModelArrayList[position].getNames()
                        apPaterno = imageModelArrayList[position].getApPaternoo()
                        apMaterno = imageModelArrayList[position].getApMaternoo()
                        correo = imageModelArrayList[position].getCorreoo()
                        estaciones = imageModelArrayList[position].getStationn()
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
        itemTouchHelper.attachToRecyclerView(binding.reciclerListSuper)
    }

    private fun loadReciclerSupervisorConsultaU() {
        imageModelArrayList = listSuperConsultaU()
        adapter = SimpleAdapterSuper(requireActivity().applicationContext!!, imageModelArrayList)
        binding.reciclerListSuper.adapter = adapter!!
        binding.reciclerListSuper.addOnItemTouchListener(
            RecyclerTouchListener(
                requireActivity().applicationContext,
                binding.reciclerListSuper,
                object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                        nameSuper = imageModelArrayList[position].getNames()
                        apPaterno = imageModelArrayList[position].getApPaternoo()
                        apMaterno = imageModelArrayList[position].getApMaternoo()
                        correo = imageModelArrayList[position].getCorreoo()
                        estaciones = imageModelArrayList[position].getStationn()
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
        itemTouchHelper.attachToRecyclerView(binding.reciclerListSuper)
    }

    private fun loadServices() {
        val request =
            ConsultaUsuarioRequest(idUsuario = com.venturessoft.human.utils.User.idUsuario)
        listaSupervisorFragmentViewModel.getConsultaUsuarioService(request)
    }

    private fun listSuper(): java.util.ArrayList<SearchModel> {
        val list = java.util.ArrayList<SearchModel>()
        for (i in 0 until busquedaUsuarioResponse.usuarioSuper!!.size) {
            val imageModel = SearchModel()
            imageModel.setNames(busquedaUsuarioResponse.usuarioSuper!![i].nombre)
            imageModel.setApPaternoo(busquedaUsuarioResponse.usuarioSuper!![i].apellidoPat)
            if (busquedaUsuarioResponse.usuarioSuper!![i].apellidoMat.isBlank().not()) {
                imageModel.setApMaternoo(busquedaUsuarioResponse.usuarioSuper!![i].apellidoMat)
            } else {
                imageModel.setApMaternoo("")
            }
            imageModel.setStatuss(busquedaUsuarioResponse.usuarioSuper!![i].estatus)
            imageModel.setIdd(busquedaUsuarioResponse.usuarioSuper!![i].idUsuario.toString())
            imageModel.setCorreoo(busquedaUsuarioResponse.usuarioSuper!![i].correo)
            imageModel.setStationn(busquedaUsuarioResponse.usuarioSuper!![i].estaciones!!)
            list.add(imageModel)
        }
        return list
    }

    private fun listSuperConsultaU(): java.util.ArrayList<SearchModel> {
        val list = java.util.ArrayList<SearchModel>()
        if (!consultaUsuarioResponse.supervisor.isNullOrEmpty()) {
            for (i in 0 until consultaUsuarioResponse.supervisor!!.size) {
                val imageModel = SearchModel()
                imageModel.setNames(consultaUsuarioResponse.supervisor!![i].nombre)
                imageModel.setApPaternoo(consultaUsuarioResponse.supervisor!![i].apellidoPat)
                if (consultaUsuarioResponse.supervisor!![i].apellidoMat!!.isBlank().not()) {
                    imageModel.setApMaternoo(consultaUsuarioResponse.supervisor!![i].apellidoMat!!)
                } else {
                    imageModel.setApMaternoo("")
                }
                imageModel.setStatuss(consultaUsuarioResponse.supervisor!![i].estatus!!)
                imageModel.setIdd(consultaUsuarioResponse.supervisor!![i].idUsuario.toString())
                imageModel.setCorreoo(consultaUsuarioResponse.supervisor!![i].correo!!)
                imageModel.setStationn(consultaUsuarioResponse.supervisor!![i].estaciones!!)
                list.add(imageModel)
            }
        }
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = Preferences.getUser(requireActivity().applicationContext)
        listenButtons()
        mListener?.onBackMenuLsuper()
        backButton()
        listenerbtnEditSuper()
    }

    private fun backButton() {
        binding.btnBackMenuListSuper.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment").commit()
        }
    }

    private fun registerSupervisor() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.Fragpricipal, RegisterSupervisorFragment()).commit()
    }

    private fun listenButtons() {
        binding.addSuperSpiner.setOnClickListener {
            if (com.venturessoft.human.utils.User.freeTrial) {
                if (consultaUsuarioResponse.supervisor?.size == null || consultaUsuarioResponse.supervisor.isNullOrEmpty()) {
                    registerSupervisor()
                } else {
                    if (consultaUsuarioResponse.supervisor!!.size >= 1) {
                        val modalBottomSheet = UpdateEnterpriceFragment()
                        modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
                    } else {
                        registerSupervisor()
                    }
                }
            } else {
                registerSupervisor()
            }
        }
        binding.etFilter.doOnTextChanged { text, start, before, count ->
            if (!text.isNullOrEmpty()) {
                val request = BusquedaUsuarioRequest(
                    idUsuario = com.venturessoft.human.utils.User.idUsuario,
                    filtroUsuario = text.toString(), pagina = 0, numRegistros = 0
                )
                listaSupervisorFragmentViewModel.getBuscarSuperService(request)
            } else {
                loadServices()
            }
        }
    }

    private fun listenerbtnEditSuper() {
        binding.btnGoToEditSuper.setOnClickListener {
            val editSupervisorFragment = EditSupervisorFragment()
            val bundle = Bundle()
            bundle.putString("idSelected", idSelected)
            bundle.putString("nameSuper", nameSuper)
            bundle.putString("apMaterno", apMaterno)
            bundle.putString("apPaterno", apPaterno)
            bundle.putString("correo", correo)
            bundle.putBoolean("estaciones", estaciones!!)
            editSupervisorFragment.arguments = bundle
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, editSupervisorFragment, "WelcomeFragment")
                .commit()
        }
        binding.btnStatus.setOnClickListener {
            val request = ActualizarUsuarioRequest(
                idUsuario = idSelected.toLong(),
                email = correo,
                nombre = nombre,
                apellidoPat = apellidoPat,
                apellidoMat = apellidoMat,
                estatus = status,
                idZonasEliminar = idZonasEliminar
            )
            listaSupervisorFragmentViewModel.getActualizarUsuarioService(request)
        }
        binding.btnDeleteSuper.setOnClickListener {
            val request = EliminaUsuarioRequest(idUsuario = idSelected.toInt())
            listaSupervisorFragmentViewModel.getEliminaUsuarioService(request)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    fun eliminarEmpleado(id: String) {
        idSelected = id
        val btnDelete = myContext.findViewById<Button>(R.id.btnDeleteSuper)
        btnDelete.callOnClick()
    }

    fun changeStatus(id: String, statusId: String) {
        idSelected = id
        status = statusId
        val btnStatus = myContext.findViewById<Button>(R.id.btnStatus)
        btnStatus.callOnClick()
    }

    fun editSuper(id: String) {
        val btnEditSuoer = myContext.findViewById<Button>(R.id.btnGoToEditSuper)
        idSelected = id
        btnEditSuoer.callOnClick()
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationListSuper.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationListSuper.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(
                requireActivity().getString(identificadorMensaje),
                requireActivity(),
                childFragmentManager
            )
        } else {
            Utilities.showDialog(code.toString(), requireActivity(), childFragmentManager)
        }
        if (adapter != null) {
            adapter!!.refreshRecicler()
        }
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.list_super_title))
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}
