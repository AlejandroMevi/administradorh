package com.venturessoft.human.views.ui.fragments.ChangePassword

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentChangePasswordBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.models.request.ActualizarPasswordRequest
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.ChangePasswordFragmentViewModel

class ChangePasswordFragment : androidx.fragment.app.Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    var user: User? = null
    private var changePasswordFragmentViewModel = ChangePasswordFragmentViewModel()
    private var passwordActualkt: String = ""
    private var newPasswordkt: String = ""
    private var newPasswordConfirmkt: String = ""
    private var longitud8: Boolean = false
    private var mayuscula: Boolean = false
    var numero: Boolean = false
    private var minuscula: Boolean = false
    private var caracteSpecial: Boolean = false
    var passwordConfirm: Boolean = false
    private var mainInterface: MainInterface? = null

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    private lateinit var binding: FragmentChangePasswordBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backButtonChangePassMethod")
        listenButtons()
        addObserver()
    }

    private fun listenButtons() {
        binding.btnChange.isEnabled = false
        binding.newPassword.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.newPassword.text.isNullOrEmpty()) {
                    validatePass()
                    binding.btnChange.isEnabled = false
                } else {
                    binding.headerRulesPassword.visibility = View.VISIBLE
                    binding.btnChange.isEnabled = validatePass()
                }

            }
        })
        binding.newPasswordConfirm.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (binding.newPassword.text.toString().length == binding.newPasswordConfirm.text?.length && binding.newPasswordConfirm.text.toString() == binding.newPassword.text.toString()) {
                    passwordConfirm = true

                } else if (binding.newPassword.text.toString().length == binding.newPasswordConfirm.text?.length) {
                    Toast.makeText(
                        activity!!,
                        getString(R.string.change_password_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        binding.btnChange.setOnClickListener {
            passwordActualkt = binding.passwordActual.text.toString()
            newPasswordkt = binding.newPassword.text.toString()
            newPasswordConfirmkt = binding.newPasswordConfirm.text.toString()
            val checkFields = checkFields(passwordActualkt, newPasswordkt)
            if (checkFields == "OK" && longitud8 && numero && mayuscula && minuscula && caracteSpecial && passwordConfirm) {
                val request = ActualizarPasswordRequest(
                    email = com.venturessoft.human.utils.User.email,
                    password = passwordActualkt,
                    passwordNuevo = newPasswordkt
                )
                changePasswordFragmentViewModel.getActualizarPasswordService(request)
            } else {
                Toast.makeText(activity, checkFields, Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnBackChange.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
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

    fun validatePass(): Boolean {
        val regexLong = ".{8,14}".toRegex()
        longitud8 = if (regexLong.matches(binding.newPassword.text.toString())) {
            binding.check1.setImageResource(R.drawable.ic_check_circle_filled)
            binding.filterA.setTextColor(requireContext().getColor(R.color.mBlack))
            true
        } else {
            binding.check1.setImageResource(R.drawable.ic_check_password)
            binding.filterA.setTextColor(requireContext().getColor(R.color.numEmployee))
            false
        }
        val regexNum = ".*\\d.*".toRegex()
        val regexMinus = ".*[a-z].*".toRegex()
        val regexMayus = ".*[A-Z].*".toRegex()
        val regexCharacter = ".*[!¡@#\$%^&*+=?_-].*".toRegex()
        numero = if (regexNum.matches(binding.newPassword.text.toString())) {
            binding.check2.setImageResource(R.drawable.ic_check_circle_filled)
            binding.filterB.setTextColor(requireContext().getColor(R.color.mBlack))
            true
        } else {
            binding.check2.setImageResource(R.drawable.ic_check_password)
            binding.filterB.setTextColor(requireContext().getColor(R.color.numEmployee))
            false
        }
        mayuscula = if (regexMayus.matches(binding.newPassword.text.toString())) {
            binding.check3.setImageResource(R.drawable.ic_check_circle_filled)
            binding.filterC.setTextColor(requireContext().getColor(R.color.mBlack))
            true
        } else {
            binding.check3.setImageResource(R.drawable.ic_check_password)
            binding.filterC.setTextColor(requireContext().getColor(R.color.numEmployee))
            false

        }
        minuscula = if (regexMinus.matches(binding.newPassword.text.toString())) {
            binding.check4.setImageResource(R.drawable.ic_check_circle_filled)
            binding.filterD.setTextColor(requireContext().getColor(R.color.mBlack))
            true
        } else {
            binding.check4.setImageResource(R.drawable.ic_check_password)
            binding.filterD.setTextColor(requireContext().getColor(R.color.numEmployee))
            false

        }
        caracteSpecial = if (regexCharacter.matches(binding.newPassword.text.toString())) {
            binding.check5.setImageResource(R.drawable.ic_check_circle_filled)
            binding.filterE.setTextColor(requireContext().getColor(R.color.mBlack))
            true
        } else {
            binding.check5.setImageResource(R.drawable.ic_check_password)
            binding.filterE.setTextColor(requireContext().getColor(R.color.numEmployee))
            false
        }
        if (longitud8 && numero && mayuscula && minuscula && caracteSpecial) {
            return true
        }
        return false
    }

    fun checkFields(passwordActual: String, newPassword: String): String {
        if (passwordActual.isEmpty() || passwordActual.isBlank()) {
            return getString(R.string.error_change_password_actual_empty)
        }
        if (newPassword.isEmpty() || newPassword.isBlank()) {
            return getString(R.string.error_change_password_actual_empty)
        }
        if (binding.newPasswordConfirm.text.toString().isBlank()) {
            return getString(R.string.a_login_error_nip_empty)
        }
        if (newPassword != binding.newPasswordConfirm.text.toString()) {
            return getString(R.string.change_password_error)
        }
        return "OK"
    }

    private fun addObserver() {
        changePasswordFragmentViewModel.actualizarPasswordResponseMutableData.observe(
            viewLifecycleOwner
        ) {
            succesResponse(it.codigo)
        }
        changePasswordFragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        changePasswordFragmentViewModel.codeError.observe(viewLifecycleOwner) {
            loadServerMessageError(it)
        }
    }

    private fun succesResponse(codigo: String) {
        if (codigo == "ET000") {
            val dialogLogout = DialogGeneral(
                getString(R.string.errormissing),
                getString(R.string.change_password_success),
                requireContext().getString(R.string.ok),
                null,
                {
                    mListener?.onBack("backToSuper")
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                        .commit()
                }
            )
            dialogLogout.show(childFragmentManager, "")
        } else {
            Utilities.loadMessageError(codigo, requireActivity(), childFragmentManager)
        }
    }

    fun showLoadingAnimation() {
        binding.loadingAnimationChagePasswordFragment.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimationChagePasswordFragment.root.visibility = View.GONE
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
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.change_password_name))
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}

