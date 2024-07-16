package com.venturessoft.human.views.ui.fragments.photographyauthorization

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentPhotographyAuthorizationBinding
import com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportesEstacionesLibres.ui.PhotoAutAdapter
import com.venturessoft.human.models.Response.LstFotoLocalItem
import com.venturessoft.human.models.request.ListPhotoAuthRequest
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportFragment
import com.venturessoft.human.views.ui.fragments.WelcomeVinculadoFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.PhotographyAuthorizationViewModel

class PhotographyAuthorizationFragment : Fragment(), PhotoAutAdapter.OnClickListener {

    private lateinit var binding: FragmentPhotographyAuthorizationBinding
    private var photographyAuthorizationViewModel = PhotographyAuthorizationViewModel()
    private var mListener: ReportFragment.OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object{
        var fotoActual: String = ""
        var fotoNueva: String = ""
        var nombreEmpleado: String = ""
        var idCia: Long = 0
        var idEmpleado: Long = 0
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPhotographyAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backPhotoToHome")
        listenButtons()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReportFragment.OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.side_menu_title_13))
        addObservers()
        getListPhoto()
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
    private fun listenButtons() {
        binding.btnBackListPhoto.setOnClickListener {
            when (User.ambiente) {
                "HU" -> {
                    Utilities.loadFragment(this, WelcomeVinculadoFragment())
                }

                "ET" -> {
                    Utilities.loadFragment(this,WelcomeFragment())
                }
            }
        }
    }
    private fun getListPhoto() {
        val listPhotoAuthRequest = ListPhotoAuthRequest(User.scia!![0].cia,  User.idUsuario)
        photographyAuthorizationViewModel.getListPhotoService(listPhotoAuthRequest)
    }

    private fun addObservers() {
        photographyAuthorizationViewModel.listPhotoData.observe(viewLifecycleOwner) { response ->
            if (response.codigo == "ET000") {
                if (response != null) {
                    if (!response.lstFotoLocal.isNullOrEmpty()) {
                        binding.employePhotoLocal.visibility = View.VISIBLE
                        binding.tvDataEmpty.root.visibility = View.GONE
                        val list = java.util.ArrayList<LstFotoLocalItem>()
                        for (i in response.lstFotoLocal.indices) {
                            val dataModel = LstFotoLocalItem()
                            dataModel.fotoActual = response.lstFotoLocal[i]?.fotoActual
                            dataModel.fotoNueva = response.lstFotoLocal[i]?.fotoNueva
                            dataModel.nombreEmpleado = response.lstFotoLocal[i]?.nombreEmpleado
                            dataModel.idCia = response.lstFotoLocal[i]?.idCia
                            dataModel.idEmpleado = response.lstFotoLocal[i]?.idEmpleado
                            list.add(dataModel)
                        }
                        setAdapter(list)
                    }else{
                        binding.employePhotoLocal.visibility = View.GONE
                        binding.tvDataEmpty.root.visibility = View.VISIBLE
                    }
                }
            } else {
                response.codigo?.let { Utilities.loadMessageError(it, requireActivity(), childFragmentManager) }
            }
        }

        photographyAuthorizationViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.INVISIBLE
            }
        }

        photographyAuthorizationViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }
    private fun loadServerMessageError(code: Int) {
        binding.progress.visibility = View.INVISIBLE
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje =
            requireContext().resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(requireActivity().getString(identificadorMensaje), requireActivity(),childFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }
    private fun setAdapter(list: ArrayList<LstFotoLocalItem>) {
        binding.employePhotoLocal.adapter = PhotoAutAdapter(list, this)
    }

    override fun onClick(lstFotoLocalItem: LstFotoLocalItem) {
        val fragmentOrigin = DetailsPhotographyAuthorizationFragment()
        nombreEmpleado = lstFotoLocalItem.nombreEmpleado.toString()
        idCia = lstFotoLocalItem.idCia?.toLong() ?: 0
        idEmpleado = lstFotoLocalItem.idEmpleado?.toLong() ?: 0
        fotoActual = lstFotoLocalItem.fotoActual.toString()
        fotoNueva = lstFotoLocalItem.fotoNueva.toString()
        Utilities.loadFragment(this, fragmentOrigin)
    }
}