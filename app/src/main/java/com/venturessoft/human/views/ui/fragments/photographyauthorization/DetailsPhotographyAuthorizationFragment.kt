package com.venturessoft.human.views.ui.fragments.photographyauthorization

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentDetailsPhotographyAuthorizationBinding
import com.venturessoft.human.models.request.PhotoAuthRequest
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.AuditHistory.ViewImageFragment
import com.venturessoft.human.views.ui.fragments.photographyauthorization.PhotographyAuthorizationFragment.Companion.fotoNueva
import com.venturessoft.human.views.ui.fragments.photographyauthorization.PhotographyAuthorizationFragment.Companion.idEmpleado
import com.venturessoft.human.views.ui.fragments.photographyauthorization.PhotographyAuthorizationFragment.Companion.nombreEmpleado
import com.venturessoft.human.views.ui.viewModels.PhotographyAuthorizationViewModel

class DetailsPhotographyAuthorizationFragment : Fragment() {
    private lateinit var binding: FragmentDetailsPhotographyAuthorizationBinding
    private var photographyAuthorizationViewModel = PhotographyAuthorizationViewModel()
    private var mainInterface: MainInterface? = null
    private var mListener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsPhotographyAuthorizationBinding.inflate(inflater, container, false)
        this.activeBack()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backListDetailsPhoto")
        addObservers()
        loadData()
        listenButtons()
    }
    private fun activeBack() {
        binding.btnBackListDetailsPhoto.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, PhotographyAuthorizationFragment(), "WelcomeFragment")
                .commit()
        }
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.side_menu_title_13))
        if (AnimotionLottie.redirect == "ListEmployee") {
            AnimotionLottie.redirect = ""
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, PhotographyAuthorizationFragment(), "WelcomeFragment")
                .commit()
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

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
    private fun loadData() {
        if (PhotographyAuthorizationFragment.fotoActual.isNotEmpty()) {
            binding.fotoActual.visibility= View.VISIBLE
            binding.txtfotoActual.visibility= View.VISIBLE
            try {
                if (PhotographyAuthorizationFragment.fotoActual.contains("File not foundjava", ignoreCase = true)
                    || PhotographyAuthorizationFragment.fotoActual.isEmpty()) {
                    binding.fotoActual.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.avartar_))
                } else {
                    val imagenBase64 = Base64.decode(PhotographyAuthorizationFragment.fotoActual, Base64.DEFAULT)
                    val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                    binding.fotoActual.setImageBitmap(imagenconverBitmap)
                }
            } catch (e: Exception) {
                println(e)
            }
        }else{
            binding.fotoActual.visibility= View.GONE
            binding.txtfotoActual.visibility= View.GONE
        }
        if (fotoNueva.isNotEmpty()) {
            try {
                if (fotoNueva.contains("File not foundjava", ignoreCase = true) || fotoNueva.isEmpty()) {
                    binding.fotoNueva.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.avartar_))
                } else {
                    val imagenBase64 = Base64.decode(fotoNueva, Base64.DEFAULT)
                    val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                    binding.fotoNueva.setImageBitmap(imagenconverBitmap)
                }
            } catch (e: Exception) {
                println(e)
            }
        }

        if (idEmpleado.toString().isNotEmpty() || idEmpleado.toString() != "null") {
            binding.numEmployee.text = idEmpleado.toString()
        }
        if (nombreEmpleado.isNotEmpty()) {
            binding.nameEmployee.text = nombreEmpleado
        }
    }
    private fun listenButtons() {
        binding.btnAcept.setOnClickListener { getPhotoAuth("S") }
        binding.btnCancel.setOnClickListener { getPhotoAuth("N") }
        binding.fotoNueva.setOnClickListener { if (fotoNueva.isNotEmpty()) viewImageD(fotoNueva) else viewImageD(null) }
        binding.fotoActual.setOnClickListener {
            if (PhotographyAuthorizationFragment.fotoActual.isNotEmpty())
                viewImageD(PhotographyAuthorizationFragment.fotoActual)
            else viewImageD(null)  }
    }

    private fun viewImageD(photo: String?) {
        val fullScreenDialogFragment = ViewImageFragment(photo)
        fullScreenDialogFragment.show(
            requireActivity().supportFragmentManager,
            "FullScreenDialogFragment"
        )
    }
    private fun success(){
        val intent = Intent(activity, AnimotionLottie::class.java)
        intent.putExtra("Redirect", "PhotoAut")
        startActivity(intent)
    }

    private fun getPhotoAuth(auth: String) {
        val reportStationFree = PhotoAuthRequest(User.scia!![0].cia,
            idEmpleado, auth)
        photographyAuthorizationViewModel.authorizationPhotoService(reportStationFree)
    }

    private fun addObservers() {
        photographyAuthorizationViewModel.listPhotoData.observe(viewLifecycleOwner) { response ->
            if (response.codigo == "ET000") {
                if (response != null) {
                    success()
                }
            } else {
                response.codigo?.let { Utilities.loadMessageError(it, requireActivity(), childFragmentManager) }
            }
        }
        photographyAuthorizationViewModel.authorizationPhoto.observe(viewLifecycleOwner) { response ->
            if (response.codigo == "ET000") {
                if (response != null) {
                    success()
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
}