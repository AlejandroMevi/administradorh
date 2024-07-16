package com.venturessoft.human.views.ui.fragments.Zones


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentSelectZonesBinding
import com.venturessoft.human.models.User
import com.venturessoft.human.utils.Preferences
import com.venturessoft.human.utils.bd.DBDemo
import com.venturessoft.human.views.ui.fragments.Supervisor.EditSupervisorFragment
import com.venturessoft.human.views.ui.fragments.Supervisor.RegisterSupervisorFragment

class SelectZonesFragment : DialogFragment(){
    private var mListener: OnFragmentInteractionListener? = null
    var user: User? = null
    companion object {
        lateinit var myContext: Activity
        var redirect: String = ""
        var nItems = ArrayList<Int>()
        lateinit var myDataBase: DBDemo
        fun newInstance(): SelectZonesFragment {
            val fragment = SelectZonesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myDataBase = DBDemo(requireActivity())
        myContext = requireActivity()
    }
    private lateinit var binding : FragmentSelectZonesBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSelectZonesBinding.inflate(inflater, container, false)
        user = Preferences.getUser(requireActivity().applicationContext)
        loadReciclerData()
        return binding.root
    }
    private fun loadReciclerData() {
        binding.reciclerZones.addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(requireActivity(), androidx.recyclerview.widget.DividerItemDecoration.VERTICAL))
        binding.reciclerZones.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(requireActivity())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = Preferences.getUser(requireActivity().applicationContext)
        mListener?.onBack("backSuper")
        binding.btnSelectZones.setOnClickListener {
            if (redirect == "EditSupervisor") {
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.Fragpricipal, EditSupervisorFragment(), "RegisterEmployee")
                    .commit()
            }else{
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, RegisterSupervisorFragment())
                        .commit()
            }
        }
        binding.saveSelectedZones.setOnClickListener {
            if (redirect == "EditSupervisor") {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, EditSupervisorFragment(), "RegisterEmployee")
                        .commit()
            } else {
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.Fragpricipal, RegisterSupervisorFragment())
                        .commit()
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }
    fun addZones(position: Int) {
        nItems.add(position)
    }
    fun emptyZones() {
        val saveData = myContext.findViewById<Button>(R.id.saveSelectedZones)
        saveData.isEnabled = nItems.size != 0
    }
}
