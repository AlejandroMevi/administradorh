package com.venturessoft.human.views.ui.fragments.Settings

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.venturessoft.human.R
import com.venturessoft.human.bd.BDPayments
import com.venturessoft.human.databinding.FragmentSettingsBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.services.Lenguaje
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.MyContextWrapper
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.utils.Utilities.Companion.countryList
import com.venturessoft.human.views.ui.activities.AnimotionLottie
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment
import java.util.Locale

class SettingsFragment : Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    var user: User? = null
    private var lenguaje: Lenguaje? = null
    private var countrySelectedElement: Int = 0
    var len = Lenguaje()
    lateinit var locale:Locale
    private var mainInterface: MainInterface? = null
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    companion object {
        lateinit var myDataBase: BDPayments
        private const val ARG_USER = "userInfo"
        fun newInstance(loggedUser: User): SettingsFragment {
            val fragment = SettingsFragment()
            val args = Bundle()
            args.putSerializable(ARG_USER, loggedUser)
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var binding : FragmentSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataBase = BDPayments(requireActivity())
        len.idioma = "es"
        len.idiomaReport = "en"
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = Preferences.getUser(requireActivity().applicationContext)
        lenguaje = Preferences.getLanguage(requireActivity().applicationContext)
        myDataBase = BDPayments(requireActivity())

        loadDatabase()
        loadContry()
        loadLenguage()
        loadLenguageReport()
        loadDataTypeUser()
        loadButton()
        mListener!!.onBack("backMenuSettings")
        loadBack()

        binding.saveContry.setOnClickListener {
            val locale = Locale(len.idioma)
            Locale.setDefault(locale)
            val config = requireActivity().baseContext.resources.configuration
            config.locale = locale
            val context: Context = MyContextWrapper.wrap(activity, len.idioma)
            resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)
            Preferences.editLenguaje(len, requireActivity())
            if (myDataBase.getNumTotalRegistrosSettings(com.venturessoft.human.utils.User.idUsuario.toString()) > 0) {
                myDataBase.editCountryAndLanguage(language = len.idioma, country = countrySelectedElement.toString(), idUsuario = com.venturessoft.human.utils.User.idUsuario.toString(), lenguajeReport = len.idiomaReport)
            } else {
                myDataBase.insertarSettings(lenguaje = len.idioma, Pais = countrySelectedElement.toString(), idUser = com.venturessoft.human.utils.User.idUsuario.toString(), lenguajeReport = len.idiomaReport)
            }
            val intent = Intent(activity, AnimotionLottie::class.java)
            intent.putExtra("Redirect", "loadLanguage")
            startActivity(intent)
        }
    }
    private fun loadButton(){
        binding.btnDelete.setOnClickListener {
            val modalBottomSheet = ConfirmCancellationFragment()
            modalBottomSheet.isCancelable = false
            modalBottomSheet.show(requireActivity().supportFragmentManager, "ModalBottomSheet.TAG")
        }
    }
    private fun loadBack() {
        binding.btnBackMenuSettings.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                .commit()
        }
    }
    private fun loadLenguageReport() {
        val lenguajes = arrayOf(getString(R.string.settings_spanish), getString(R.string.settings_english), getString(R.string.settings_portuguese)/*, "Frances"*/)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, lenguajes)
        binding.lenguageSelectedReport.setAdapter(adapter)
        val item = when (len.idiomaReport) {
            "en" -> 1
            "pt" -> 2
            "gsw" -> 3
            else -> 0
        }
        binding.lenguageSelectedReport.setText(lenguajes[item],false)
        binding.lenguageSelectedReport.setOnItemClickListener { _, _, position, _ ->
            len.idiomaReport = when (position) {
                0 -> "es"
                1 -> "en"
                2 -> "pt"
                else -> "fr"
            }
        }
    }
    private fun loadContry() {
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, countryList)
        binding.contrySelected.setAdapter(adapter)
        binding.contrySelected.setText(countryList[countrySelectedElement],false)
        binding.contrySelected.setOnItemClickListener { _, _, position, _ ->
            countrySelectedElement = position
        }
    }
    private fun loadLenguage() {
        val lenguajes = arrayOf(getString(R.string.settings_spanish), getString(R.string.settings_english), getString(R.string.settings_portuguese)/*, getString(R.string.settings_french)*/)
        val adapter = ArrayAdapter(requireActivity(), android.R.layout.simple_dropdown_item_1line, lenguajes)
        binding.lenguageSelected.setAdapter(adapter)
        val item = when (len.idioma) {
            "en" -> 1
            "pt" -> 2
            "gsw" -> 3
            else -> 0
        }
        binding.lenguageSelected.setText(lenguajes[item],false)
        binding.lenguageSelected.setOnItemClickListener { _, _, position, _ ->
            len.idioma = when (position) {
                0 -> "es"
                1 -> "en"
                2 -> "pt"
                else -> "fr"
            }
        }
    }
    private fun loadDataTypeUser() {
        when (com.venturessoft.human.utils.User.ambiente) {
            "HU" -> {
                binding.lenguageSelectedReport.visibility = View.GONE
                binding.linearLenguageReport.visibility = View.GONE
                binding.linearCountry.visibility = View.GONE
                binding.contrySelected.visibility = View.GONE
            }
            "ET" -> {
                if (com.venturessoft.human.utils.User.tipoUsuario != 1) {
                    binding.linearCountry.visibility = View.GONE
                    binding.contrySelected.visibility = View.GONE
                }
            }
        }
    }
    private fun loadDatabase() {
        val rows = myDataBase.getPorIdUser(com.venturessoft.human.utils.User.idUsuario.toString())
        while (rows.moveToNext()) {
            countrySelectedElement = rows.getString(3).toInt()
            len.idioma = rows.getString(2)
            len.idiomaReport = rows.getString(5)
        }
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.side_menu_title_10))
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
        mListener = null
        mainInterface = null
    }
}
