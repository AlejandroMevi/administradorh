package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentAuditHistoryFilterBinding
import com.venturessoft.human.models.request.ListaAuditoriaRequest
import com.venturessoft.human.models.response.ListaAuditoriaResponse
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.WelcomeVinculadoFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import com.venturessoft.human.views.ui.viewModels.AuditHistoryViewModel
import java.text.SimpleDateFormat
import java.util.*

class AuditHistoryFilterFragment : Fragment() {

    private lateinit var binding: FragmentAuditHistoryFilterBinding
    private var auditHistoryViewModel = AuditHistoryViewModel()
    private var mListener: OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null
    private var dateFormatIn: String? = null
    private var dateFormatUp: String? = null

    companion object {
        var listaAuditoriaResponse = ListaAuditoriaResponse()
    }

    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuditHistoryFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mListener?.onBack("BackHistoryFilterToHomeFragment")
        listenButtons()
        loadCalendarFrom()
        loadCalendarUp()
        addObservers()
    }

    private fun addObservers() {
        auditHistoryViewModel.listaAuditoriaMutableData.observe(viewLifecycleOwner) {
            listaAuditoriaResponse = it
            if (listaAuditoriaResponse.codigo == "ET000") {
                if (listaAuditoriaResponse.auditoria != null) {
                    val bundle = Bundle()
                    bundle.putSerializable(
                        "listaAuditoriaResponse",
                        listaAuditoriaResponse.auditoria
                    )
                    val listAudtHistoryFragment = ListAudtHistoryFragment()
                    listAudtHistoryFragment.arguments = bundle
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, listAudtHistoryFragment, "WelcomeFragment")
                        .commit()
                } else {
                    Utilities.loadMessageError("ET327", requireActivity(), childFragmentManager)
                }
            } else {
                Utilities.loadMessageError(
                    listaAuditoriaResponse.codigo,
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
        auditHistoryViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                showLoadingAnimation()
            } else {
                hideLoadingAnimation()
            }
        }

    }

    private fun listenButtons() {
        binding.btnBackHistoryFilter.setOnClickListener {
            when (User.ambiente) {
                "HU" -> {
                    loadFragment(WelcomeVinculadoFragment())
                }

                "ET" -> {
                    loadFragment(WelcomeFragment())
                }
            }
        }
        binding.btnApply.setOnClickListener {
            when (User.ambiente) {
                "HU" -> {
                    val request = ListaAuditoriaRequest(
                        fechaInicio = dateFormatIn!!,
                        fechaFin = dateFormatUp!!,
                        numCia = User.ciaVinculado,
                        idUsuario = User.idUsuario,
                        numEmp = if (binding.numEmploye.text.toString().trim().isNotEmpty()) {
                            binding.numEmploye.text.toString().toLong()
                        } else {
                            null
                        }
                    )
                    auditHistoryViewModel.getListAuditoriaService(request)
                }

                "ET" -> {
                    val request = ListaAuditoriaRequest(
                        fechaInicio = dateFormatIn!!,
                        fechaFin = dateFormatUp!!,
                        numCia = User.scia!![0].cia,
                        idUsuario = User.idUsuario,
                        numEmp = if (binding.numEmploye.text.toString().trim().isNotEmpty()) {
                            binding.numEmploye.text.toString().toLong()
                        } else {
                            null
                        }
                    )
                    auditHistoryViewModel.getListAuditoriaService(request)
                }
            }
        }
        binding.checkBoxAllEmployees.setOnClickListener {
            if (binding.checkBoxAllEmployees.isChecked) {
                binding.tilNumEmploye.isVisible = false
            } else {
                binding.tilNumEmploye.isVisible = true
                binding.numEmploye.setText("")
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun loadCalendarFrom() {
        val format2 = SimpleDateFormat("yyyy-MM-dd")
        binding.dateFrom.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        dateFormatIn = format2.format(System.currentTimeMillis())
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val dateUpConvertDate: Date =
                    SimpleDateFormat("dd/MM/yyyy").parse(binding.dateUp.text.toString()) as Date
                view.maxDate = System.currentTimeMillis()
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateObtained: Date =
                    SimpleDateFormat("dd/MM/yyyy").parse(sdf.format(cal.time)) as Date
                if (dateObtained.compareTo(Date()).toString() == "-1" && dateObtained.compareTo(
                        dateUpConvertDate
                    ).toString() != "1"
                ) {
                    binding.dateFrom.setText(sdf.format(cal.time))
                    dateFormatIn = format2.format(cal.time)
                }
            }
        binding.dateFrom.isLongClickable = false

        binding.dateFrom.setOnClickListener {
            val dialog = DatePickerDialog(
                requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    fun loadCalendarUp() {
        val format2 = SimpleDateFormat("yyyy-MM-dd")
        binding.dateFrom.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        dateFormatUp = format2.format(System.currentTimeMillis())
        binding.dateUp.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        val cal = Calendar.getInstance()
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val myFormat = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val dateObtained: Date =
                    SimpleDateFormat("dd/MM/yyyy").parse(sdf.format(cal.time)) as Date
                val dateFromConvertDate: Date =
                    SimpleDateFormat("dd/MM/yyyy").parse(binding.dateFrom.text.toString()) as Date
                if (dateObtained.compareTo(Date()).toString() == "-1") {
                    binding.dateUp.setText(sdf.format(cal.time))
                    dateFormatUp = format2.format(cal.time)
                }
                if (dateObtained.compareTo(dateFromConvertDate).toString() == "-1") {
                    binding.dateUp.setText(sdf.format(Date()))
                    dateFormatUp = format2.format(Date())
                }
            }

        binding.dateUp.isLongClickable = false

        binding.dateUp.setOnClickListener {
            val dialog = DatePickerDialog(
                requireActivity(), dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }
    }

    fun showLoadingAnimation() {
        binding.loadingAnimation.root.visibility = View.VISIBLE
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    fun hideLoadingAnimation() {
        binding.loadingAnimation.root.visibility = View.GONE
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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
        if (context is MainInterface) {
            mainInterface = context
        }
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.details_audit_historial_title))
    }

    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}