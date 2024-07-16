package com.venturessoft.human.views.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ActivityForgotNipBinding
import com.venturessoft.human.models.request.ConsultaTokeNoLoginRequest
import com.venturessoft.human.models.request.RecuperarPasswordRequest
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.Companion.observeOnce
import com.venturessoft.human.views.ui.viewModels.CreateAccountViewModel
import com.venturessoft.human.views.ui.viewModels.ForgotNipActivityViewModel
import java.util.Locale

class ForgotNipActivity : BaseActivity() {

    private lateinit var binding: ActivityForgotNipBinding
    private var emailTitlee: String = ""
    private var forgotNipActivityViewModel = ForgotNipActivityViewModel()
    private val vmCreateAccount: CreateAccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotNipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Utilities.setupUI(binding.parentLayout, this)
        addObserver()
        listenButton()
    }
    private fun listenButton() {
        binding.btnSaveAdmin.setOnClickListener { resetNip() }
        binding.materialToolbar.setNavigationOnClickListener { back() }
    }
    private fun back() { finish() }
    private fun resetNip() {
        emailTitlee = binding.emailTextFieldForgot.text.toString()
        val checkFieldsMessage = checkFields()
        if (checkFieldsMessage == "OK") {
            val requestConsultaTokenNoLogin = ConsultaTokeNoLoginRequest()
            vmCreateAccount.getConsultaTokenNoLogin(requestConsultaTokenNoLogin)
            vmCreateAccount.consultaTokenNoLoginResponseMutableData.observeOnce(this) {
                if (it != null) {
                    if (it.codigo == "ET000") {
                        val request = RecuperarPasswordRequest(email = emailTitlee)
                        forgotNipActivityViewModel.getRecuperarPasswordService(request, it.token)
                    } else {
                        loadMessageError(it.codigo)
                    }
                }
            }
        } else {
            Toast.makeText(this, checkFieldsMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadMessageError(code: String) {
        val contextoPaquete: String = this.packageName
        val identificadorMensaje = this.resources.getIdentifier(code, "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(
                getString(identificadorMensaje),
                this,
                supportFragmentManager
            )
        } else {
            Utilities.showDialog("" + code, this, supportFragmentManager)
        }
        hideLoadingAnimation()
    }

    fun checkFields(): String {
        val correoValido = "@" in binding.emailTextFieldForgot.text.toString()
        if (binding.emailTextFieldForgot.text.toString().isEmpty()) {
            return getString(R.string.a_login_error_email_empty)
        }
        if (!correoValido) {
            return getString(R.string.a_login_error_email_valids)
        }
        return "OK"
    }

    private fun addObserver() {
        forgotNipActivityViewModel.isLoading.observe(this) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }

        forgotNipActivityViewModel.recuperarPasswordResponseMutableData.observe(this) {
            if (it.codigo == "ET000" || it.message == "ET000") {
                actionSucces()
            } else {
                val contextoPaquete: String = this.packageName
                var code = ""
                if (it.message.isNotEmpty()) {
                    code = it.message
                }
                if (it.codigo.isNotEmpty()) {
                    code = it.codigo
                }
                val identificadorMensaje =
                    this.resources.getIdentifier(code, "string", contextoPaquete)
                if (identificadorMensaje > 0) {
                    Utilities.showDialog(this.getString(identificadorMensaje), this,supportFragmentManager)
                } else {
                    Utilities.showDialog(it.message, this,supportFragmentManager)
                }
            }
        }
        forgotNipActivityViewModel.codeError.observe(this) {
            loadServerMessageError(it)
        }

    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = this.packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(this.getString(identificadorMensaje), this,supportFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), this,supportFragmentManager)
        }
    }

    private fun actionSucces() {
        hideLoadingAnimation()
        val dialogLogout = DialogGeneral(
            getString(R.string.errormissing),
            getString(R.string.recover_password_success).replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            },
            null,
            null,
            {
                finish()
            }
        )
        dialogLogout.show(supportFragmentManager,"")
    }
    fun showLoadingAnimation() { binding.progress.root.isVisible = true }
    fun hideLoadingAnimation() { binding.progress.root.isVisible = false }
}