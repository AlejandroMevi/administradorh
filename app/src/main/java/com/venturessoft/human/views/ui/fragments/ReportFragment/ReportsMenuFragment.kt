package com.venturessoft.human.views.ui.fragments.ReportFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentReportsMenuBinding
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.adapters.ReportsMenuAdapter
import com.venturessoft.human.views.ui.fragments.Filter.FilterReportFragment
import com.venturessoft.human.views.ui.fragments.WelcomeVinculadoFragment
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment

class ReportsMenuFragment : Fragment() {
    private lateinit var binding: FragmentReportsMenuBinding
    private var mListener: ReportFragment.OnFragmentInteractionListener? = null
    private var mainInterface: MainInterface? = null
    private lateinit var menu:Array<String>
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentReportsMenuBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mListener?.onBack("backMenuToHome")
        binding.reciclerReportsMenu.layoutManager = LinearLayoutManager(context)
        loadMenuRecicler()
        listenButtons()
    }
    private fun loadMenuRecicler() {
        when (User.ambiente) {
            "HU" -> {
                menu = arrayOf(getString(R.string.reportsmenufragment_item_1),getString(R.string.reportsmenufragment_item_2), requireActivity().getString(R.string.reportsmenufragment_item_3))
            }
            "ET"->{
                menu = arrayOf(requireActivity().getString(R.string.reportsmenufragment_item_1), requireActivity().getString(R.string.reportsmenufragment_item_2), requireActivity().getString(R.string.reportsmenufragment_item_3))
            }
        }
        binding.reciclerReportsMenu.adapter = ReportsMenuAdapter(menu)
    }
    private fun listenButtons() {
        binding.btnBackHome.setOnClickListener {
            when (User.ambiente) {
                "HU" -> {
                    loadFragment(WelcomeVinculadoFragment())
                }
                "ET"->{
                    loadFragment(WelcomeFragment())
                }
            }
        }
        binding.reciclerReportsMenu.addOnItemTouchListener(
            Utilities.Companion.RecyclerTouchListener(requireActivity().applicationContext, binding.reciclerReportsMenu, object : ClickListener {
                override fun onClick(view: View, position: Int) {
                    val nameReport = view.findViewById<TextView>(R.id.name)
                    when (nameReport.text) {
                        getString(R.string.reportsmenufragment_item_1)->{
                            loadFragment(FilterReportFragment())
                        }
                        getString(R.string.reportsmenufragment_item_2)->{
                            loadFragment(Enroll_Unenroll_ReportFragment())
                        }
                        getString(R.string.reportsmenufragment_item_3)-> {
                            val filterReport = FilterReportFragment()
                            val bundle = Bundle()
                            bundle.putString("ReportSelect", getString(R.string.reportsmenufragment_item_3))
                            filterReport.arguments = bundle
                            loadFragment(filterReport)
                        }
                    }
                }
                override fun onLongClick(view: View?, position: Int) {}
            })
        )
    }
    private fun loadFragment(fragmet: Fragment) {
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, fragmet, "WelcomeFragment")
                .commit()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ReportFragment.OnFragmentInteractionListener) {
            mListener = context
        }
        if (context is MainInterface) {
            mainInterface = context
        }
    }
    interface ClickListener {
        fun onClick(view: View, position: Int)

        fun onLongClick(view: View?, position: Int)
    }
    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.reportsmenufragment_title))
    }
    override fun onDetach() {
        super.onDetach()
        mainInterface = null
    }
}