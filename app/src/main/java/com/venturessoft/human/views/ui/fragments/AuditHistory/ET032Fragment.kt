package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentET032Binding
import com.venturessoft.human.models.response.itemAuditoriasDetalle
import com.venturessoft.human.utils.Utilities

class ET032Fragment : Fragment() {
    private var photoCheck: String?=""
    private var photoEnroll: String?=""
    private var movimiento: String?=""
    private var mListener: OnFragmentInteractionListener? = null
    private var auditoriaDet: ArrayList<itemAuditoriasDetalle>? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
    }
    private lateinit var binding : FragmentET032Binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentET032Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        mListener?.onBack("ET032Fragment")

        binding.btnBackET032.setOnClickListener {
            loadFragment(ListAudtHistoryFragment())
        }
        auditoriaDet = ListAudtHistoryFragment.detallesResponse.auditoriaDet
        loadMovimiento()
        loadData()
        listenButtons()

    }

    private fun loadMovimiento() {
        movimiento = if (auditoriaDet!![0].tipo == "E"){
            getString(R.string.checkin)
        }else{
            getString(R.string.checkout)
        }
    }

    private fun listenButtons() {
        binding.employePhotoCheck.setOnClickListener {
            if (photoCheck!!.isNotEmpty()){
                viewImageD(photoCheck!!)

            }else{
                viewImageD(null)
            }
        }
        binding.employePhotoEnrroll.setOnClickListener {
            if (photoEnroll!!.isNotEmpty()){
                viewImageD(photoEnroll!!)
            }else{
                viewImageD(null)
            }

        }
    }

    private fun loadData() {
        binding.usernameText.text = if (auditoriaDet!![0].nombreEmp.contains("null")) auditoriaDet!![0].nombreEmp.replace("null","") else auditoriaDet!![0].nombreEmp
        binding.movText.text = movimiento+ " "+ auditoriaDet!![0].fechaMov
        binding.txtDateCheck.text = auditoriaDet!![0].fechaChecada
        binding.idNumCompany.text = auditoriaDet!![0].numCia.toString()
        binding.idNumEmp.text = auditoriaDet!![0].numEmp.toString()
        binding.device.text = auditoriaDet!![0].dispositivo

        photoCheck = auditoriaDet!![0].fotografia
       val imageCheck = Utilities.Base64StringToBitmap(photoCheck!!)
        binding.employePhotoCheck.setImageBitmap(imageCheck)
        if (auditoriaDet!![0].fotoEnrolada.trim().isNotEmpty()) {
            photoEnroll = auditoriaDet!![0].fotoEnrolada
            val imageEnroll = Utilities.Base64StringToBitmap(photoEnroll!!)
            binding.employePhotoEnrroll.setImageBitmap(imageEnroll)
        }

    }

    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }
    private fun viewImageD(photo: String?) {
        val fullScreenDialogFragment = ViewImageFragment(photo)
        fullScreenDialogFragment.show(
            requireActivity().supportFragmentManager,
            "FullScreenDialogFragment"
        )
    }
}