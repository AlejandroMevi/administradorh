package com.venturessoft.human.views.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.ActivityChangeNipBinding
import com.venturessoft.human.models.request.CambiarNipRequest
import com.venturessoft.human.models.response.CambiarNipResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.viewModels.ChangeNipVinculadoViewModel

private lateinit var binding: ActivityChangeNipBinding

class ChangeNipVinculadoActivity : AppCompatActivity() {
    var t = Throwable()
    private var changeNipVinculadoViewModel = ChangeNipVinculadoViewModel()
    private var cambiarNipResponse = CambiarNipResponse()
    var idioma: String = "ES"
    private lateinit var btnBackChangeNipV: Button
    private lateinit var navigationDrawerButton: ImageView

    companion object {
        lateinit var myDataBase: BDPayments
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeNipBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBase = BDPayments(this)
        loadDatabase()
        listenButtons()
        addObservers()
        mayusculas()
    }

    override fun onResume() {
        super.onResume()
        binding.toolBar.title.text = getString(R.string.change_password_name)
    }

    private fun listenButtons() {
        btnBackChangeNipV = this.findViewById(R.id.back_arrow_btn) as Button
        navigationDrawerButton = this.findViewById(R.id.navigationDrawerButton) as ImageView
        navigationDrawerButton.visibility = View.GONE
        btnBackChangeNipV.visibility = View.VISIBLE
        btnBackChangeNipV.setOnClickListener {
            val intent = Intent(this, NavigationDrawerActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.btnChangeNipV.setOnClickListener {
            val checkFieldsMessage = checkFields()
            if (checkFieldsMessage == "OK") {
                val request = CambiarNipRequest(
                    claveCompania = User.claveCia,
                    numEmp = User.idUsuario,
                    campo1 = binding.rfcTextField.text.toString().trim(),
                    campo2 = binding.curpTextField.text.toString().trim(),
                    nipActual = binding.currentNipTextField.text.toString().trim(),
                    nipNuevo = binding.newNipTextField.text.toString().trim(),
                    idioma = idioma.trim()
                )
                changeNipVinculadoViewModel.getCambiarNip(request)
            } else {

                Toast.makeText(this, checkFieldsMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addObservers() {
        changeNipVinculadoViewModel.cambiarNipResponseMutableData.observe(this) {
            cambiarNipResponse = it
            if (cambiarNipResponse.codigo == "ET000") {
                val dialogLogout = DialogGeneral(
                    getString(R.string.errormissing),
                    getString(R.string.change_password_success),
                    null,
                    null,
                    {
                        finish()
                    }
                )
                dialogLogout.show(supportFragmentManager, "")
            } else {
                Toast.makeText(this, "Favor de ingresar correctos los datos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        changeNipVinculadoViewModel.isLoading.observe(this) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        changeNipVinculadoViewModel.codeError.observe(this) {
            loadServerMessageError(it)
        }
    }

    private fun showLoadingAnimation() {
        binding.animationChangeNipV.root.visibility = View.VISIBLE
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideLoadingAnimation() {
        binding.animationChangeNipV.root.visibility = View.GONE
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageError(code: Int) {
        hideLoadingAnimation()
        val contextoPaquete: String = this.packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(this.getString(identificadorMensaje), this, supportFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), this, supportFragmentManager)
        }
    }

    private fun loadDatabase() {
        val rows = myDataBase.getPorIdUser(User.idUsuario.toString())
        while (rows.moveToNext()) {
            idioma = rows.getString(2).toString().toUpperCase()
        }
    }

    fun checkFields(): String {
        if (binding.currentNipTextField.text.isNullOrEmpty()) {
            return getString(R.string.error_current_nip_empty)
        }
        if (binding.newNipTextField.text.isNullOrEmpty()) {
            return getString(R.string.error_new_nip_empty)
        }
        if (binding.confirmNipTextField.text.isNullOrEmpty()) {
            return getString(R.string.error_confirm_nip_empty)
        }
        if (binding.newNipTextField.text.toString() != binding.confirmNipTextField.text.toString()) {
            return getString(R.string.error_new_confirm_nip)
        }
        if (binding.rfcTextField.text.isNullOrEmpty()) {
            return getString(R.string.error_rfc_empty)
        }
        if (binding.curpTextField.text.isNullOrEmpty()) {
            return getString(R.string.error_curp_empty)
        }
        return "OK"
    }

    private fun mayusculas() {
        binding.rfcTextField.filters += InputFilter.AllCaps()
        binding.curpTextField.filters += InputFilter.AllCaps()
    }
}
