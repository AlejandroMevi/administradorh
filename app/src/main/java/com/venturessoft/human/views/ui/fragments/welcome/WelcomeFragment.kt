package com.venturessoft.human.views.ui.fragments.welcome

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.estimote.coresdk.cloud.internal.ApiUtils
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.navigation.NavigationView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentWelcomeBinding
import com.venturessoft.human.models.request.BuscarEmpleadoVinRequest
import com.venturessoft.human.models.request.ConsultaEstadisticaRequest
import com.venturessoft.human.models.request.ListPhotoAuthRequest
import com.venturessoft.human.models.response.*
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.PercentFormatter
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.HomeActivity.Companion.firstTime
import com.venturessoft.human.views.ui.activities.WayToPayActivity
import com.venturessoft.human.views.ui.fragments.BaseFragment
import com.venturessoft.human.views.ui.fragments.UpdatePhoto.UpdatePhotoFragment
import com.venturessoft.human.views.ui.viewModels.PhotographyAuthorizationViewModel
import com.venturessoft.human.views.ui.viewModels.WelcomeViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class WelcomeFragment : BaseFragment(), OnChartValueSelectedListener {
    private var photographyAuthorizationViewModel = PhotographyAuthorizationViewModel()
    private var mListener: OnFragmentInteractionListener? = null
    private var enrroll: String = ""
    var idioma: String = "ES"
    lateinit var progressDialog: ProgressDialog
    private var mainInterface: MainInterface? = null
    private var consultaEstadisticaResponse: ConsultaEstadisticaResponse? = null
    private var buscarEmpleadoVinResponse = BuscarEmpleadoVinResponse()
    private var saldoResponse: EmpleadosPayResponse? = null
    private var consultationViewModel = WelcomeViewModel()
    private var maxEmpl: Double? = null

    interface OnFragmentInteractionListener {
        fun onSlaveUserActive(slaveActive: Boolean)
    }

    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = ProgressDialog(activity, R.style.MyAlertDialogStyle)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showPrivacyNotice()
        loadTitleWelcome()
        loadEnvironment()
        Utilities.setupUI(view, requireActivity())
        if (!User.expiracion && !User.freeTrial){
            println("expiracion ${User.expiracion} freeTrial ${User.freeTrial}")
        }else{
            if (User.freeTrial) freeTrail() else expired()
        }
        listensButtons()
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar("")
        try {
            autorizationRemaining()
        }catch (e: Exception){
            println("Exception null User.cia y User.usuario $e")
        }
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

    private fun autorizationRemaining() {
        val listPhotoAuthRequest = ListPhotoAuthRequest(User.scia!![0].cia,  User.idUsuario)
        photographyAuthorizationViewModel.getListPhotoService(listPhotoAuthRequest)
        photographyAuthorizationViewModel.listPhotoData.observe(viewLifecycleOwner) { response ->
            if (response.codigo == "ET000") {
                if (response != null) {
                    if (!response.lstFotoLocal.isNullOrEmpty()){
                        val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
                        val menuItem = navigationView.menu.findItem(R.id.nav_PhotographyAuthorization)
                        val badgeTextView = LayoutInflater.from(requireContext()).inflate(R.layout.badge_layout, navigationView, false) as TextView
                        badgeTextView.background = ContextCompat.getDrawable(requireContext(), R.drawable.badge_background)
                        badgeTextView.text = response.lstFotoLocal.size.toString()
                        menuItem.actionView = badgeTextView
                    }else{
                        val navigationView: NavigationView = requireActivity().findViewById(R.id.nav_view)
                        val menuItem = navigationView.menu.findItem(R.id.nav_PhotographyAuthorization)
                        menuItem.actionView = null
                    }
                }
            } else {
                println("response.codigo" + response.codigo)
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun loadPieChart() {
        val checadasRestantes = ArrayList<String>()
        checadasRestantes.add(getString(R.string.data_admin_graphic_legent_remaining))
        checadasRestantes.add(getString(R.string.data_admin_graphic_legent_consumed))
        val noOfEmpPie = ArrayList<Entry>()
        val availablePercent: Float
        val spentPercent: Float

        if (saldoResponse!!.empleadosEstadistica?.get(0)?.maximoEmpleados == 0 || saldoResponse!!.empleadosEstadistica?.get(
                0
            )?.porcentaje == 0f
        ) {
            spentPercent = 0f
            availablePercent = 100f
        } else {
            spentPercent = saldoResponse!!.empleadosEstadistica?.get(0)?.porcentaje!!
            availablePercent = 100f - spentPercent
        }
        val totalTrabajadores = saldoResponse!!.empleadosEstadistica?.get(0)?.maximoEmpleados!!
        maxEmpl = saldoResponse!!.empleadosEstadistica?.get(0)?.maximoEmpleados?.toDouble()

        noOfEmpPie.add(Entry(availablePercent, 0))
        noOfEmpPie.add(Entry(spentPercent, 1))
        binding.pieChar.setUsePercentValues(true)
        binding.pieChar.setDescription("")
        val dataSet = PieDataSet(noOfEmpPie, "")
        dataSet.valueFormatter = PercentFormatter()
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 8f
        val dataPie = PieData(checadasRestantes, dataSet)
        binding.pieChar.data = dataPie
        binding.pieChar.legend.position = Legend.LegendPosition.BELOW_CHART_CENTER
        binding.pieChar.legend.isEnabled = false
        val libertyColors = intArrayOf(
            Color.rgb(18, 187, 230), Color.rgb(238, 92, 166), Color.rgb(136, 180, 187),
            Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)
        )
        dataSet.setColors(libertyColors)

        binding.pieChar.animateXY(3000, 3000)
    }

    private fun loadBarChart() {
        val noOfEmp = ArrayList<BarEntry>()
        val year = ArrayList<String>()
        val dateFormatOrigin = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dateFormatTarget = SimpleDateFormat("dd/MM", Locale.getDefault())
        for (i in 0..6) {
            consultaEstadisticaResponse!!.diasEstadisticaList!![i].porcentaje
            noOfEmp.add(
                BarEntry(
                    consultaEstadisticaResponse!!.diasEstadisticaList!![i].porcentaje.toFloat(), i,
                    consultaEstadisticaResponse!!.diasEstadisticaList!![i].totalEmpleadosChecados
                )
            )
            val dtStart = consultaEstadisticaResponse!!.diasEstadisticaList!![i].fecha
            val date = dateFormatOrigin.parse(dtStart)
            year.add(dateFormatTarget.format(date as Date))
        }
        val bardataset = BarDataSet(noOfEmp, null)
        binding.graficChard.animateY(5000)
        binding.graficChard.setVisibleXRangeMaximum(7f)
        binding.graficChard.setVisibleYRangeMaximum(100f, YAxis.AxisDependency.LEFT)
        binding.graficChard.axisLeft.setLabelCount(5, true)
        binding.graficChard.axisLeft.mAxisRange = 25f
        binding.graficChard.axisLeft.setAxisMinValue(0f)
        binding.graficChard.axisLeft.setAxisMaxValue(100f)
        binding.graficChard.axisRight.setLabelCount(5, true)
        binding.graficChard.axisRight.mAxisRange = 25f
        binding.graficChard.axisRight.setAxisMinValue(0f)
        binding.graficChard.axisRight.setAxisMaxValue(100f)
        binding.graficChard.xAxis.setLabelsToSkip(0)
        binding.graficChard.isScaleXEnabled = false
        binding.graficChard.isScaleYEnabled = false
        binding.graficChard.axisRight.isEnabled = false
        binding.graficChard.setDescription("")
        val data = BarData(year, bardataset)
        val color =
            ContextCompat.getColor(requireActivity().applicationContext, R.color.colorPrimary)
        binding.graficChard.notifyDataSetChanged()
        bardataset.color = color
        binding.graficChard.data = data
        binding.graficChard.legend.isEnabled = false
        binding.graficChard.setOnChartValueSelectedListener(this)
    }


    private fun expired() {
        val dateView = Utilities.cambiarFormatoFecha(User.fechaVigencia)
        val daysUntilEnd: Int = Utilities.compareDates(User.fechaActual, User.fechaVigencia)
        //val daysUntilEnd: Int = Utilities.compareDates("2023-08-30", "2023-08-21")
        when {
            daysUntilEnd >= 8 -> {
                // No mostrar nada antes de los 8 días de la fecha de fin de suscripción
                binding.linearFreeTrial.cardFree.isVisible = false
            }

            daysUntilEnd in 1..7 -> {
                // Mostrar mensaje para los últimos 7 días de la suscripción
                val dialog = Dialog(requireActivity())

                dialog.setContentView(R.layout.dialog_expired_time)
                dialog.window?.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                )

                dialog.window?.setBackgroundDrawable(ColorDrawable(0))

                dialog.window?.setWindowAnimations(R.style.AnimationsForDialog)

                val closeBtn: ImageView = dialog.findViewById(R.id.closeDialog)
                val payNow: Button = dialog.findViewById(R.id.payNow)
                val hi: TextView = dialog.findViewById(R.id.txtFreeTrial)
                val date: TextView = dialog.findViewById(R.id.txtFreeTrialDuration)
                val formatoImporte: NumberFormat =
                    NumberFormat.getCurrencyInstance(Locale("es", "MX"))
                val costo = (User.maximoEmpleados * User.costoEmpleado)
                val totFactura = if (User.maximoEmpleados.toString().isNotEmpty()) "de ${
                    formatoImporte.format(costo)
                }" else ""
                binding.linearFreeTrial.icClose.isVisible = true
                binding.linearFreeTrial.icClose.setOnClickListener {
                    binding.linearFreeTrial.cardFree.isVisible = false
                }
                hi.text = getString(R.string.expired_hi, User.nombre)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    date.text = Html.fromHtml(
                        getString(R.string.expired_istoday, totFactura, dateView),
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    date.text =
                        Html.fromHtml(getString(R.string.expired_istoday, totFactura, dateView))
                }

                payNow.setOnClickListener {
                    dialog.cancel()
                    val intent = Intent(requireActivity(), WayToPayActivity::class.java)
                    intent.putExtra("isPaypal", false)
                    startActivity(intent)
                }
                closeBtn.setOnClickListener { dialog.cancel() }
                dialog.show()
            }

            daysUntilEnd == 0 -> {
                // Mostrar mensaje el día de la fecha de fin de suscripción
                binding.linearFreeTrial.icClose.isVisible = true
                binding.linearFreeTrial.icClose.setOnClickListener {
                    binding.linearFreeTrial.cardFree.isVisible = false
                }
                val typeface =
                    ResourcesCompat.getFont(requireContext(), R.font.robotoflex)
                binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.info_white)
                binding.linearFreeTrial.backgroundImage.setImageResource(R.drawable.forma_free_trial__fase_2)
                binding.linearFreeTrial.txtFreeTrial.typeface = typeface
                binding.linearFreeTrial.txtFreeTrialDuration.typeface = typeface
                binding.linearFreeTrial.cardFree.isVisible = true
                binding.linearFreeTrial.txtFreeTrial.text =
                    getString(R.string.expired_hi, User.nombre)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(
                            getString(R.string.expired_today_date, dateView),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                } else {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(getString(R.string.expired_today_date, dateView))
                }
                binding.linearFreeTrial.actualizarAhora.text =
                    getString(R.string.expired_pay_now)
            }

            daysUntilEnd in -7..-1 -> {
                yelowCard()
            }

            else -> {
                expiredTime()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun freeTrail() {
        if (User.freeTrial) {
            if (User.fechaVigencia.isNullOrEmpty()) {
                if (firstTime == 0) {
                    val modalBottomSheet = TrialDetailsFragment()
                    modalBottomSheet.isCancelable = false
                    modalBottomSheet.show(childFragmentManager, "ModalBottomSheet.TAG")
                    firstTime = 1
                }
                firstViewFreeTrail()
            } else {
                binding.linearFreeTrial.cardFree.isVisible = true
                val days: Int = Utilities.compareDates(User.fechaActual, User.fechaVigencia)
                /*val days: Int = Utilities.compareDates("2023-08-18", "2023-08-15")*/
                when {
                    days >= 15 -> {
                        firstViewFreeTrail()
                    }

                    days in 14 downTo 6 -> {
                        // Mostrar mensaje para los primeros 10 días de la prueba gratis
                        firstViewFreeTrail()
                    }

                    days in 5 downTo 1 -> {
                        // Mostrar mensaje para los ultimos 5 días de la prueba gratis
                        if (days == 1) binding.linearFreeTrial.txtFreeTrialDuration.text =
                            getString(R.string.free_trail_time_one_day, days)
                        else binding.linearFreeTrial.txtFreeTrialDuration.text =
                            getString(R.string.free_trail_time, days)
                    }

                    days == 0 -> {
                        yelowCard()
                    }

                    else -> {
                        expiredTime()
                    }
                }
            }
        } else {
            binding.linearFreeTrial.cardFree.isVisible = false
        }
    }

    private fun listensButtons() {
        binding.okButton.isClickable = true
        binding.linearFreeTrial.actualizarAhora.setOnClickListener {

            if (User.freeTrial) mainInterface?.showLanding(true) else {
                val intent = Intent(requireActivity(), WayToPayActivity::class.java)
                intent.putExtra("isPaypal", false)
                startActivity(intent)
            }
        }
        binding.okButton.setOnClickListener {
            val employeeId = binding.etAdminEmployee.text.toString()
            if (employeeId.isEmpty()) {
                Toast.makeText(context, R.string.error_admin_employee_empty, Toast.LENGTH_LONG)
                    .show()
            } else {
                when (User.ambiente) {
                    "ET" -> {
                        val request = BuscarEmpleadoVinRequest(
                            ciaNom = User.scia!![0].cia.toInt(),
                            numEmp = employeeId.toLong(),
                            fechaFoto = "1",
                            idioma = idioma
                        )
                        consultationViewModel.getBuscarEmpleadoVin(request)
                    }
                }
            }
        }
    }

    private fun loadEnvironment() {
        when (User.ambiente) {
            "HU" -> {
                loadDataSuperVinculado()
            }

            "ET" -> {
                setLayoutAndUserType(User.tipoUsuario)
                callServices()
                addObservers()
            }
        }
    }

    private fun loadTitleWelcome() {
        if (User.ambiente == "ET") {
            binding.razonTitleText.text = User.scia!![0].razonSocial
        } else {
            binding.userName.visibility = View.VISIBLE
        }
    }

    private fun callServices() {
        val statisticalConsultationRequest = ConsultaEstadisticaRequest(User.scia!![0].cia, 7)
        consultationViewModel.getStatisticalConsultationService(statisticalConsultationRequest)
        User.idCompani?.let { consultationViewModel.getSaldoService(it) }
    }

    private fun addObservers() {
        consultationViewModel.consultationResponseMutableData.observe(viewLifecycleOwner) {
            consultaEstadisticaResponse = it
            if (consultaEstadisticaResponse!!.codigo == "ET000") {
                loadBarChart()
            } else {
                Utilities.loadMessageError(
                    consultaEstadisticaResponse!!.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        consultationViewModel.saldoResponseMutableData.observe(viewLifecycleOwner) {
            it.codigo
            saldoResponse = it
            if (saldoResponse!!.codigo == "ET000") {
                loadPieChart()
            } else {
                saldoResponse!!.codigo?.let { it1 ->
                    Utilities.loadMessageError(
                        it1,
                        requireActivity(),
                        childFragmentManager
                    )
                }
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
        consultationViewModel.buscarEmpleadoVinMutableData.observe(viewLifecycleOwner) {
            buscarEmpleadoVinResponse = it
            if (buscarEmpleadoVinResponse.errorMessage == "ET000") {
                nextViewProfileEmployee()
            } else {
                Utilities.loadMessageError(
                    buscarEmpleadoVinResponse.errorMessage,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        consultationViewModel.buscarEmpleadoVinMutableData.observe(viewLifecycleOwner) {

        }
    }

    private fun setLayoutAndUserType(userType: Int) {
        when (userType) {
            2 -> loadDataSuper()
            1 -> loadDataAdmin()
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

            "ET" -> {
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

    @SuppressLint("SetTextI18n")
    private fun loadDataAdmin() {
        val nombreCompleto =
            "${User.nombre} ${User.apellidoPat} " + if (User.apellidoMat.isBlank().not()) {
                User.apellidoMat
            } else {
                ""
            }
        binding.userName.text = getString(R.string.welcome, nombreCompleto)
        binding.dateTimeLayout.visibility = View.GONE
        binding.adminFlowInfoLayout.visibility = View.GONE
        binding.messageRequiredAction.text = enrroll
        binding.okButton.visibility = View.GONE
        binding.chartBar.visibility = View.VISIBLE
        binding.checkHistoryText.visibility = View.VISIBLE
        binding.pieBarLayout.visibility = View.VISIBLE
        binding.adminTitle.visibility = View.VISIBLE
        //binding.pieLegent.visibility = View.VISIBLE
        binding.graficChard.visibility = View.VISIBLE
        binding.lineCenter.visibility = View.VISIBLE
        binding.userName.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun loadDataSuper() {
        binding.checkHistoryText.visibility = View.INVISIBLE
        enrroll = getString(R.string.message_enroll)
        val nombreCompleto =
            "${User.nombre} ${User.apellidoPat} " + if (User.apellidoMat.isBlank().not()) {
                User.apellidoMat
            } else {
                ""
            }
        binding.userName.text = getString(R.string.welcome, nombreCompleto)
        binding.dateTimeLayout.visibility = View.VISIBLE
        binding.adminTitle.visibility = View.INVISIBLE
        binding.adminFlowInfoLayout.visibility = View.VISIBLE
        binding.lineCenter.visibility = View.INVISIBLE
        binding.messageRequiredAction.text = enrroll
        mListener?.onSlaveUserActive(slaveActive = false)
        binding.userName.visibility = View.VISIBLE
        binding.razonTitleText.visibility = View.GONE
    }

    private fun loadDataSuperVinculado() {
        binding.razonTitleText.visibility = View.GONE
        binding.checkHistoryText.visibility = View.INVISIBLE
        enrroll = getString(R.string.message_enroll)
        binding.userName.text = getString(R.string.welcome, User.nombreCompleto)
        binding.dateTimeLayout.visibility = View.VISIBLE
        binding.adminTitle.visibility = View.INVISIBLE
        binding.adminFlowInfoLayout.visibility = View.VISIBLE
        binding.lineCenter.visibility = View.INVISIBLE
        binding.messageRequiredAction.text = enrroll
        mListener?.onSlaveUserActive(slaveActive = false)
    }

    internal inner class progressPress : AsyncTask<Void, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            progressDialog.show()
            progressDialog.setContentView(R.layout.custom_progressdialog)
            progressDialog.setCancelable(false)
        }

        override fun doInBackground(vararg params: Void?): String {
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (result != "") {
                progressDialog.dismiss()
            }
        }
    }

    private fun generateCenterSpannableText(): SpannableString {
        val s = SpannableString("MPAndroidChart")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 14, 0)
        s.setSpan(StyleSpan(Typeface.NORMAL), 14, s.length - 15, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 14, s.length - 15, 0)
        s.setSpan(RelativeSizeSpan(.8f), 14, s.length - 15, 0)
        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 14, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 14, s.length, 0)
        return s
    }

    override fun onNothingSelected() {}
    override fun onValueSelected(e: Entry?, dataSetIndex: Int, h: Highlight?) {
        binding.graficChard.highlightValue(h)
        Utilities.showDialog(getString(R.string.welcome_message_barchar) + " " + e?.data.toString(), requireActivity(),childFragmentManager)
    }

    private fun showLoadingAnimation() {
        binding.loadingAnimationWelcome.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideLoadingAnimation() {
        binding.loadingAnimationWelcome.root.visibility = View.GONE
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

    private fun firstViewFreeTrail() {
        // No mostrar nada antes de los 15 días de la fecha de fin de suscripción
        binding.linearFreeTrial.cardFree.isVisible = User.freeTrial
        binding.linearFreeTrial.txtFreeTrialDuration.isVisible = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.linearFreeTrial.cardFree.setCardBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.card_free_fase_1,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.txtFreeTrial.setTextColor(
                requireActivity().resources.getColor(
                    R.color.card_free_1_text,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.txtFreeTrialDuration.setTextColor(
                requireActivity().resources.getColor(
                    R.color.card_free_1_text,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.time_freetrial_icon_blue)
            binding.linearFreeTrial.backgroundImage.setImageResource(R.drawable.forma_free_trial__fase_1_)
            binding.linearFreeTrial.txtFreeTrial.text =
                getString(R.string.free_trail_active)
        } else {
            binding.linearFreeTrial.cardFree.setCardBackgroundColor(
                ContextCompat.getColor(requireContext(), R.color.card_free_fase_1)
            )
            binding.linearFreeTrial.txtFreeTrial.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_1_text
                )
            )
            binding.linearFreeTrial.txtFreeTrialDuration.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_1_text
                )
            )
            binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.time_freetrial_icon_blue)
            binding.linearFreeTrial.backgroundImage.setImageResource(R.drawable.forma_free_trial__fase_1_)
            binding.linearFreeTrial.txtFreeTrial.text =
                getString(R.string.free_trail_active)
        }
    }

    private fun yelowCard() {
        // Mostrar mensaje para los dias de tolerancia al fin de la suscripción
        val dateView = Utilities.cambiarFormatoFecha(User.fechaVigencia)

        binding.linearFreeTrial.icClose.setOnClickListener {
            binding.linearFreeTrial.cardFree.isVisible = false
        }
        binding.linearFreeTrial.actualizarAhora.setBackgroundResource(R.drawable.borde_redondo_yellow)
        binding.linearFreeTrial.backgroundImage.setImageResource(R.drawable.forma_fondo_alerta_pago_atrasado__fase_1_)
        binding.linearFreeTrial.icClose.setImageResource(R.drawable.ic_close_yello)
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.robotoflex)
        binding.linearFreeTrial.txtFreeTrial.typeface = typeface
        binding.linearFreeTrial.txtFreeTrialDuration.typeface = typeface
        binding.linearFreeTrial.cardFree.isVisible = true
        binding.linearFreeTrial.icClose.isVisible = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.linearFreeTrial.cardFree.setCardBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.card_free_expired_day_1_to_7__back,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.txtFreeTrial.setTextColor(
                requireActivity().resources.getColor(
                    R.color.card_free_1_text,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.txtFreeTrialDuration.setTextColor(
                requireActivity().resources.getColor(
                    R.color.card_free_1_text,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.error_yellow)
            if (User.freeTrial) {

                binding.linearFreeTrial.txtFreeTrial.text =
                    getString(R.string.card_view_today_title)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.linearFreeTrial.txtFreeTrialDuration.text = Html.fromHtml(
                        getString(R.string.card_view_today_txt, dateView),
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(getString(R.string.card_view_today_txt, dateView))
                }
                binding.linearFreeTrial.actualizarAhora.text = getString(R.string.cardview_botton)

            } else {
                binding.linearFreeTrial.txtFreeTrial.text =
                    getString(R.string.expired_hi, User.nombre)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    binding.linearFreeTrial.txtFreeTrialDuration.text = Html.fromHtml(
                        getString(R.string.expired_date_one_to_seven),
                        Html.FROM_HTML_MODE_LEGACY
                    )
                } else {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(getString(R.string.expired_date_one_to_seven))
                }
                binding.linearFreeTrial.actualizarAhora.text =
                    getString(R.string.expired_pay_now_two)
            }

            binding.linearFreeTrial.actualizarAhora.setTextColor(
                requireActivity().resources.getColor(
                    R.color.card_free_expired_day_1_to_7,
                    requireActivity().theme
                )
            )
        } else {
            binding.linearFreeTrial.cardFree.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_expired_day_1_to_7__back
                )
            )
            binding.linearFreeTrial.txtFreeTrial.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_1_text
                )
            )
            binding.linearFreeTrial.txtFreeTrialDuration.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_1_text
                )
            )
            binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.error_yellow)
            if (User.freeTrial) {
                binding.linearFreeTrial.txtFreeTrial.text =
                    getString(R.string.card_view_today_title)
                binding.linearFreeTrial.txtFreeTrialDuration.text =
                    Html.fromHtml(getString(R.string.card_view_today_txt, dateView))
                binding.linearFreeTrial.actualizarAhora.text = getString(R.string.cardview_botton)
            } else {
                binding.linearFreeTrial.txtFreeTrial.text =
                    getString(R.string.expired_hi, User.nombre)
                binding.linearFreeTrial.txtFreeTrialDuration.text =
                    Html.fromHtml(getString(R.string.expired_date_one_to_seven))
                binding.linearFreeTrial.actualizarAhora.text =
                    getString(R.string.expired_pay_now_two)
            }
        }
    }

    private fun expiredTime() {
        // Mostrar mensaje después de los 7 días de tolerancia al fin de la suscripción
        binding.linearFreeTrial.backgroundImage.setImageResource(R.drawable.forma_fondo_alerta_pago_atrasado_fase_2_suspendido)
        binding.linearFreeTrial.cardFree.isVisible = true
        binding.linearFreeTrial.cardFree.radius = 0f
        binding.linearFreeTrial.txtFreeTrialContacto.isVisible = true
        binding.linearFreeTrial.guideline12.setGuidelineBegin(24)
        //quitar round
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.robotoflex)
        binding.linearFreeTrial.txtFreeTrial.typeface = typeface

        val cardView = binding.linearFreeTrial.cardFree
        val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, 16, 0, 0)
        cardView.layoutParams = layoutParams
        val image = binding.linearFreeTrial.icFreeTrial
        val imagelayoutParams = image.layoutParams as ViewGroup.MarginLayoutParams
        imagelayoutParams.setMargins(0, 24, 0, 24)

        val linear = binding.linearFreeTrial.linearPeriodo
        val linearlayoutParams = linear.layoutParams as ViewGroup.MarginLayoutParams
        linearlayoutParams.setMargins(0, 24, 0, 24)

        val button = binding.linearFreeTrial.actualizarAhora
        val buttonlayoutParams = button.layoutParams as ViewGroup.MarginLayoutParams
        buttonlayoutParams.setMargins(0, 16, 0, 24)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.linearFreeTrial.cardFree.setCardBackgroundColor(
                requireActivity().resources.getColor(
                    R.color.card_free_expired_red_back,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.actualizarAhora.setBackgroundResource(R.drawable.borde_redondo_red)
            binding.linearFreeTrial.txtFreeTrial.setTextColor(
                requireActivity().resources.getColor(
                    R.color.dialog_txt,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.txtFreeTrialDuration.setTextColor(
                requireActivity().resources.getColor(
                    R.color.dialog_txt,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.txtFreeTrialContacto.setTextColor(
                requireActivity().resources.getColor(
                    R.color.dialog_txt,
                    requireActivity().theme
                )
            )
            binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.error__red)

            binding.linearFreeTrial.txtFreeTrial.text =
                getString(R.string.expired_hi, User.nombre)

            binding.linearFreeTrial.actualizarAhora.text =
                getString(R.string.expired_pay_now_two)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (User.freeTrial) {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(
                            getString(R.string.free_trail_expired),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                } else {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(
                            getString(R.string.expired_date_one_to_seven),
                            Html.FROM_HTML_MODE_LEGACY
                        )
                }


            } else {
                if (User.freeTrial) {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(getString(R.string.free_trail_expired))
                } else {
                    binding.linearFreeTrial.txtFreeTrialDuration.text =
                        Html.fromHtml(getString(R.string.expired_date_one_to_seven))
                }

            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.linearFreeTrial.txtFreeTrialContacto.text =
                    Html.fromHtml(
                        getString(R.string.expired_contact),
                        Html.FROM_HTML_MODE_LEGACY
                    )
            } else {
                binding.linearFreeTrial.txtFreeTrialContacto.text =
                    Html.fromHtml(getString(R.string.expired_contact))
            }
            binding.linearFreeTrial.actualizarAhora.setTextColor(
                requireActivity().resources.getColor(
                    R.color.card_free_expired_red,
                    requireActivity().theme
                )
            )
        } else {
            binding.linearFreeTrial.cardFree.setCardBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_expired_red_back
                )
            )
            binding.linearFreeTrial.actualizarAhora.setBackgroundResource(R.drawable.borde_redondo_red)
            binding.linearFreeTrial.txtFreeTrial.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dialog_txt
                )
            )
            binding.linearFreeTrial.txtFreeTrialDuration.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dialog_txt
                )
            )
            binding.linearFreeTrial.txtFreeTrialContacto.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.dialog_txt
                )
            )
            binding.linearFreeTrial.icFreeTrial.setImageResource(R.drawable.error__red)
            binding.linearFreeTrial.txtFreeTrial.text =
                getString(R.string.expired_hi, User.nombre)
            binding.linearFreeTrial.actualizarAhora.text =
                getString(R.string.expired_pay_now_two)
            binding.linearFreeTrial.txtFreeTrialContacto.text =
                Html.fromHtml(getString(R.string.expired_contact))
            binding.linearFreeTrial.actualizarAhora.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.card_free_expired_red
                )
            )
        }
        binding.linearFreeTrial.backgroundImage.scaleType = ImageView.ScaleType.CENTER_CROP
    }
}