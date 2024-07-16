package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentItemCheckInOutSuccessfulBinding
import com.venturessoft.human.models.response.itemAuditoriasDetalle
import com.venturessoft.human.utils.Utilities

class ItemCheckInOutSuccessFragment() : Fragment() {
    private var photoCheck: String?=""
    private var movimiento: String? = ""
    private var mListener: OnFragmentInteractionListener? = null
    private var auditoriaDet: ArrayList<itemAuditoriasDetalle>? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    private val TAG: String = FragmentItemCheckInOutSuccessfulBinding::class.java.simpleName
    private var _binding : FragmentItemCheckInOutSuccessfulBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentItemCheckInOutSuccessfulBinding.inflate(inflater, container, false)
        Log.d(TAG, "ON CREATE VIEW")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mListener?.onBack("btnBackItemCheckInOut")
        loadData()
        listenButtons()
    }

    private fun listenButtons() {
        binding.btnBackItemCheckInOut.setOnClickListener {
            loadFragment(ListAudtHistoryFragment())
        }
        binding.employePhotoCheck.setOnClickListener {
            if (photoCheck!!.isNotEmpty()){
                viewImageD(photoCheck!!)

            }else{
                viewImageD(null)
            }
        }
    }

    private fun loadData() {
        auditoriaDet = ListAudtHistoryFragment.detallesResponse.auditoriaDet
        loadMovimiento()
        binding.usernameText.text = if (auditoriaDet!![0].nombreEmp.contains("null")) auditoriaDet!![0].nombreEmp.replace("null","") else auditoriaDet!![0].nombreEmp
        binding.movText.text = movimiento+ " "+ auditoriaDet!![0].fechaMov
        binding.txtDateCheck.text =  auditoriaDet!![0].fechaChecada
        binding.idNumCompany.text = auditoriaDet!![0].numCia.toString()
        binding.idNumEmp.text = auditoriaDet!![0].numEmp.toString()
        binding.txtET.text = "${auditoriaDet!![0].codigoEtime} : "
        binding.device.text = auditoriaDet!![0].dispositivo

        /*
                val datosRecibidos = arguments?.getString("device")
                if (datosRecibidos != null) {
                    binding.device.text = datosRecibidos
                }
         */
        photoCheck = auditoriaDet!![0].fotografia
        loadPhoto()

        try {
            if (auditoriaDet!![0].estaciones.isNullOrEmpty()){
                binding.estacion.isVisible = false
                binding.txtEstacion.isVisible = false
                binding.estacion.text = "Estacion"
            }else{
                binding.estacion.text = auditoriaDet!![0].estaciones?.get(0)?.nombre
                binding.estacion.isVisible = true
                binding.txtEstacion.isVisible = true
            }
        }catch (e : Exception){
            println(e)
        }
    }

    private fun loadPhoto() {
        val photoBitmap = Utilities.Base64StringToBitmap(auditoriaDet!![0].fotografia)
        binding.employePhotoCheck.setImageBitmap(photoBitmap)
    }

    private fun loadMovimiento() {
        movimiento = if (auditoriaDet!![0].tipo == "E"){
            getString(R.string.checkin)
        }else{
            getString(R.string.checkout)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }
    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }

    private fun viewImageD(photo: String?) {
        val fullScreenDialogFragment = ViewImageFragment(photo)
        fullScreenDialogFragment.show(
            requireActivity().supportFragmentManager,
            "FullScreenDialogFragment"
        )
    }
}