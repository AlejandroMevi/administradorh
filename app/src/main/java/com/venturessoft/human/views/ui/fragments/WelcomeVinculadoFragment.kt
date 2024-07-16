package com.venturessoft.human.views.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.estimote.coresdk.cloud.internal.ApiUtils
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentWelcomeVinculadoBinding
import com.venturessoft.human.models.request.BuscarEmpleadoVinRequest
import com.venturessoft.human.models.response.*
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.UpdatePhoto.UpdatePhotoFragment
import com.venturessoft.human.views.ui.fragments.welcome.AvisoPrivacidadDialog
import com.venturessoft.human.views.ui.viewModels.WelcomeViewModel
import java.util.Locale

class WelcomeVinculadoFragment : Fragment() {
    private var consultationViewModel = WelcomeViewModel()
    private var buscarEmpleadoVinResponse = BuscarEmpleadoVinResponse()
    private var mainInterface: MainInterface? = null
    private var enrroll: String = ""
    var idioma: String = "ES"

    companion object {
        lateinit var myDataBase: BDPayments

    }

    private lateinit var binding: FragmentWelcomeVinculadoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeVinculadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDataBase = BDPayments(requireActivity())
        loadDataBase()
        loadDataSuperVinculado()
        loadServices()
        listensButtons()
        showPrivacyNotice()
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar("")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
    private fun showPrivacyNotice() {
        if (User.urlAvisoPriv.isNotEmpty()){
            val preferencias = ApiUtils.getSharedPreferences(requireContext())
            val status = preferencias.getBoolean(Constants.AVISO, false)
            if (!status){
                val fullScreenDialogFragment = AvisoPrivacidadDialog()
                fullScreenDialogFragment.show(requireActivity().supportFragmentManager, "FullScreenDialogFragment")
            }
        }
    }
    private fun loadDataBase() {
        val rows = myDataBase.getPorIdUser(User.idUsuario.toString())
        while (rows.moveToNext()) {
            idioma = rows.getString(2).toString().toUpperCase(Locale.ROOT)
        }
    }

    private fun loadServices() {
        consultationViewModel.buscarEmpleadoVinMutableData.observe(viewLifecycleOwner) {
            buscarEmpleadoVinResponse = it
            if (it.error == true) {
                Toast.makeText(requireActivity(), it.errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                nextViewProfileEmployee()
            }
        }
        consultationViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        consultationViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }


    private fun nextViewProfileEmployee() {
        val udatePhotoFragment = UpdatePhotoFragment()
        val bundle = Bundle()


        when (User.ambiente) {
            "HU" -> {
                bundle.putLong(
                    "idEmpleado",
                    binding.etAdminEmployee.text.toString().toLong()
                )
                bundle.putString("nameEmployee", buscarEmpleadoVinResponse.nombre)
                bundle.putString("foto", buscarEmpleadoVinResponse.foto)
            }
        }
        udatePhotoFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.Fragpricipal, udatePhotoFragment, "WelcomeFragment")
            .commit()

    }

    private fun loadDataSuperVinculado() {

        enrroll = getString(R.string.message_enroll)
        binding.userNameV.text = getString(R.string.welcome, User.nombreCompleto)
        binding.messageRequiredActionV.text = enrroll
    }

    private fun listensButtons() {
        binding.okButtonV.isClickable = true
        binding.okButtonV.setOnClickListener {
            val employeeId = binding.etAdminEmployee.text.toString()
            if (employeeId.isEmpty()) {
                Toast.makeText(context, R.string.error_admin_employee_empty, Toast.LENGTH_LONG)
                    .show()
            } else {
                val request = BuscarEmpleadoVinRequest(
                    ciaNom = User.ciaVinculado.toInt(),
                    numEmp = employeeId.toLong(),
                    fechaFoto = "1",
                    idioma = idioma
                )
                consultationViewModel.getBuscarEmpleadoVin(request)
            }
        }

    }

    private fun showLoadingAnimation() {
        binding.loadingAnimationWelcomeV.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

    }

    private fun hideLoadingAnimation() {
        binding.loadingAnimationWelcomeV.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = requireActivity().packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(requireActivity().getString(identificadorMensaje), requireActivity(),childFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), requireActivity(),childFragmentManager)
        }
    }

}
