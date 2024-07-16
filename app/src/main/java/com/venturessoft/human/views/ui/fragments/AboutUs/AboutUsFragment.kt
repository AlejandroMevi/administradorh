package com.venturessoft.human.views.ui.fragments.AboutUs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venturessoft.human.BuildConfig
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentAboutUsBinding
import com.venturessoft.human.utils.MainInterface
import com.venturessoft.human.views.ui.fragments.welcome.WelcomeFragment

class AboutUsFragment : androidx.fragment.app.Fragment() {
    private var mListener: OnFragmentInteractionListener? = null
    companion object {

        fun newInstance(): AboutUsFragment {
            val fragment = AboutUsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
    interface OnFragmentInteractionListener {
        fun onBack(backViewName: String)
    }
    private lateinit var binding: FragmentAboutUsBinding
    private var mainInterface: MainInterface? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numversion.text = BuildConfig.VERSION_NAME
        binding.numcompilacion.text = BuildConfig.VERSION_CODE.toString()
        listenButtons()
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
        mainInterface = null
    }

    override fun onResume() {
        super.onResume()
        mainInterface?.setTextToolbar(getString(R.string.side_menu_title_11))
    }
    private fun listenButtons() {
        mListener?.onBack("backMenuAbout")
        binding.btnBackAboutUsF.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.Fragpricipal, WelcomeFragment(), "WelcomeFragment")
                .commit()
        }
    }
}