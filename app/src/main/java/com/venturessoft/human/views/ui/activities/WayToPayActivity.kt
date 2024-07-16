package com.venturessoft.human.views.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.ActivityWayToPayBinding
import com.venturessoft.human.models.request.AdministrarCompraRequest
import com.venturessoft.human.models.response.AdministrarCompraResponse
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.Companion.dateNextMonth
import com.venturessoft.human.views.ui.fragments.paypal.PaypalSuccesFragment
import com.venturessoft.human.views.ui.fragments.welcome.EnterpriceDetailsFragment
import com.venturessoft.human.views.ui.fragments.welcome.NumberEmployeeFragment
import com.venturessoft.human.views.ui.viewModels.WayToPayActivityViewModel
import org.json.JSONException

class WayToPayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWayToPayBinding
    private var wayToPayActivityViewModel = WayToPayActivityViewModel()
    private var administrarCompraResponse = AdministrarCompraResponse()
    private val PAYPAL_REQUEST_CODE = 1
    private var totalCoste: Double = 0.0
    private var folioCompra: String = ""
    private var cancelSendOffline = false
    private var idPagoOffline: String? = ""
    protected var numTotalRowsOffline: Int = 0
    lateinit var myDataBase: BDPayments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWayToPayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myDataBase = BDPayments(this)
        val isPaypal: Boolean = intent.getBooleanExtra("isPaypal", false)
        if (isPaypal && User.freeTrial) {
            val newFragment = EnterpriceDetailsFragment(true)
            newFragment.isCancelable = false
            newFragment.show(supportFragmentManager, "game")
        }
        addObserver()
        loadOfflinePayments()
        setupView()
        loadData()
        loadBack()
        loadTypePayment()
    }

    companion object {
        var employeNumber = MutableLiveData(0)
    }

    @SuppressLint("SetTextI18n")
    private fun listPrice(){
        when (employeNumber.value) {
            in 10 downTo  0 -> User.costoEmpleado = 7.0
            in 20 downTo 11 -> User.costoEmpleado = 6.86
            in 30 downTo 21 -> User.costoEmpleado = 6.65
            in 40 downTo 31 -> User.costoEmpleado = 6.45
            in 50 downTo 41 -> User.costoEmpleado = 6.26
            in 100 downTo 51 -> User.costoEmpleado = 6.07
            in 200 downTo 101 -> User.costoEmpleado = 5.89
            in 300 downTo 201 -> User.costoEmpleado = 5.71
            in 400 downTo 301 -> User.costoEmpleado = 5.54
            in 500 downTo 401 -> User.costoEmpleado = 5.38
            in 1000 downTo 501 -> User.costoEmpleado = 5.22
            in 2000 downTo 1001 -> User.costoEmpleado = 5.06
            in 3000 downTo 2001 -> User.costoEmpleado = 4.91
            else -> User.costoEmpleado = 4.76
        }
        binding.price.text = "$${User.costoEmpleado} ${getString(R.string.waytopay_x_colaborador)}"
    }
    @SuppressLint("SetTextI18n")
    private fun addObserver() {
        employeNumber.value = User.maximoEmpleados
        listPrice()

        binding.payButton.isClickable = true
        binding.employesNumber.text =
            "${User.maximoEmpleados} ${getString(R.string.waytopay_x_colaboradores)}"
        binding.nextDate.text = "${getString(R.string.waytopay_tu_siguiente_pago)} ${
            Utilities.cambiarFormatoFecha(dateNextMonth())
        }"

        wayToPayActivityViewModel.isLoading.observe(this) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }
        wayToPayActivityViewModel.codeError.observe(this) {
            loadServerMessageError(it)
        }
        wayToPayActivityViewModel.administrarCompraResponseMutableData.observe(this) {
            administrarCompraResponse = it
            if (administrarCompraResponse.codigo == "ET000") {
                successCompra()
            } else {
                Utilities.loadMessageError(
                    administrarCompraResponse.codigo,
                    this,
                    supportFragmentManager
                )
            }
        }
        wayToPayActivityViewModel.administrarCompraOfflineResponseMutableData.observe(this) {
            if (it.codigo == "ET000") {
                Utilities.showDialog(
                    getString(R.string.a_success_payment),
                    this,
                    supportFragmentManager
                )
                myDataBase.daleteChecadaPor(idPagoOffline!!)
                verPagoYEnviar()
            } else if (it.codigo == "ET307") {
                myDataBase.daleteChecadaPor(idPagoOffline!!)
                Utilities.showDialog(
                    getString(R.string.way_offline_message_3),
                    this,
                    supportFragmentManager
                )
            }
        }
        wayToPayActivityViewModel.codeErrorOffline.observe(this) {
            loadServerMessageErrorOffline(it)
        }
    }

    private fun successCompra() {
        val modalBottomSheet = PaypalSuccesFragment(folioCompra)
        modalBottomSheet.isCancelable = false
        modalBottomSheet.show(supportFragmentManager, "ModalBottomSheet.TAG")
    }

    private fun processPayment() {
        val price = "%.2f".format(totalCoste).replace(",", ".")
        val intent = Intent(this, PayPalActivity::class.java)
        intent.putExtra("price", price)
        startActivityForResult(intent, PAYPAL_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PAYPAL_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            data.getStringExtra("code")
            val folio = data.getStringExtra("folio")
            val date = data.getStringExtra("date")
            if (folio != null && date != null) {
                showDetails(folio, date)
            }
        }
    }

    private fun loadTypePayment() {
        binding.payButton.setOnClickListener {
            processPayment()
        }
    }

    private fun loadBack() {
        binding.btnBackHome.setOnClickListener {
            val intent = Intent(this@WayToPayActivity, NavigationDrawerActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData() {
        binding.btnChange.setOnClickListener {
            val modalBottomSheet = NumberEmployeeFragment()
            modalBottomSheet.show(supportFragmentManager, "ModalBottomSheet.TAG")
        }

        binding.payButton.isEnabled = false
        binding.payButton.isClickable = false

        employeNumber.observe(this) { numberEmployes ->
            if (numberEmployes.toString().toLong() > 0) {
                listPrice()
                binding.employesNumber.text =
                    "$numberEmployes ${getString(R.string.waytopay_x_colaboradores)}"
                totalCoste = numberEmployes.toDouble() * User.costoEmpleado
                totalCoste = "%.2f".format(totalCoste).replace(",", ".").toDouble()
                binding.finalPrice.text = "\$ " + "%.2f".format(totalCoste).replace(",", ".")
                binding.payButton.isEnabled = true
                binding.payButton.isClickable = true
            } else {
                binding.finalPrice.text = "\$ 0.00"
                binding.payButton.isEnabled = false
                binding.payButton.isClickable = false
            }
        }
    }

    private fun setupView() {
        loadTypePayment()
    }

    override fun onResume() {
        super.onResume()
        if (AnimotionLottie.redirect == "Home") {
            AnimotionLottie.redirect = ""
            val intent = Intent(this, NavigationDrawerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDetails(folio: String, date: String) {
        try {
            folioCompra = folio
            val request = AdministrarCompraRequest(
                idAdmin = User.idUsuario,
                subtotal = totalCoste,
                total = "%.2f".format(totalCoste).replace(",", ".").toDouble(),
                fechaCompra = date,
                folio = folioCompra,
                numEmpleados = employeNumber.value?.toInt() ?: 0,
                cvePais = "MX"
            )
            wayToPayActivityViewModel.getAdministrarCompra(request)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun showLoadingAnimation() {
        binding.linearLoadingAnimationPay.visibility = View.VISIBLE
        binding.loadingAnimationWayToPay.root.visibility = View.VISIBLE
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideLoadingAnimation() {
        binding.linearLoadingAnimationPay.visibility = View.GONE
        binding.loadingAnimationWayToPay.root.visibility = View.GONE
        this.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    private fun loadServerMessageErrorOffline(code: Int) {
        val contextoPaquete: String = this.packageName
        val identificadorMensaje =
            this.resources.getIdentifier("ES$code", "string", contextoPaquete)
        if (identificadorMensaje > 0) {
            Utilities.showDialog(this.getString(identificadorMensaje), this, supportFragmentManager)
        } else {
            Utilities.showDialog(code.toString(), this, supportFragmentManager)
        }
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

    private fun loadOfflinePayments() {
        val hashConnectionInternet = Utilities.validateConexion(this)
        if (hashConnectionInternet) {
            val registrosObtenidos =
                BDPayments(this).getNumTotalRegistros(User.idUsuario)
            numTotalRowsOffline = registrosObtenidos
            if (registrosObtenidos > 0) {
                pendingMessageOffline()
            }
        } else {
            Toast.makeText(this, "Sin conexión a internet", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pendingMessageOffline() {
        val dialogLogout = DialogGeneral(
            getString(R.string.error),
            getString(R.string.way_offline_message_1),
            getString(R.string.accept),
            getString(R.string.no),
            {
                verPagoYEnviar()
            },
            {
                val dialogLogout = DialogGeneral(
                    getString(R.string.error),
                    getString(R.string.way_offline_message_2),
                    getString(R.string.accept),
                    getString(R.string.no),
                    null
                ) {
                    verPagoYEnviar()
                }
                dialogLogout.show(supportFragmentManager, "")
            }
        )
        dialogLogout.show(supportFragmentManager, "")
    }

    private fun verPagoYEnviar() {
        if (!cancelSendOffline) {
            val registro: Cursor?
            registro = myDataBase.getPorFechaHora(User.idUsuario)
            if (registro.moveToFirst()) {
                idPagoOffline = registro.getString(0).toInt().toString()
                val request = AdministrarCompraRequest(
                    idAdmin = registro.getString(1).toLong(),
                    subtotal = registro.getString(2).toDouble(),
                    total = registro.getString(3).toDouble(),
                    fechaCompra = registro.getString(4),
                    folio = registro.getString(5),
                    numEmpleados = registro.getString(6).toInt(),
                    cvePais = registro.getString(7)
                )
                wayToPayActivityViewModel.getAdministrarCompraOffline(request)
            }
        }
    }
}
