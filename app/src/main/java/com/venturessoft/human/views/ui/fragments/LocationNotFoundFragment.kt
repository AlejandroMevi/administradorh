package com.venturessoft.human.views.ui.fragments

import android.content.Context
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentLocationNotFoundBinding
import com.venturessoft.human.utils.DialogGeneral
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.fragments.Administrator.AdminDataFragment
import com.venturessoft.human.views.ui.fragments.Employe.EmployeeListFragment

class LocationNotFoundFragment : LocationFragment() {

    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LocationNotFoundFragment.
         */
        fun newInstance(): LocationNotFoundFragment {
            val fragment = LocationNotFoundFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var binding : FragmentLocationNotFoundBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1)
            mParam2 = requireArguments().getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLocationNotFoundBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.retryButton.setOnClickListener {
            if (Utilities.isLocationServiceActivated(requireContext())) {
                checkPermissionAndRequestLocation()
            } else {
                val dialogLogout = DialogGeneral(
                    getString(R.string.location),
                    getString(R.string.message_feature_location_activated)
                )
                dialogLogout.show(childFragmentManager,"")
            }
        }
        binding.confirmButton.setOnClickListener {
        }
    }

    fun onButtonPressed(uri: Uri) {
        mListener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onLocationFound(location: Location) {
        goToCameraFragment(location)
        println("onLocationFound$location")
    }

    override fun onLocationNotFound() {
        val dialogLogout = DialogGeneral(
            getString(R.string.location),
            getString(R.string.error_no_location_found)
        )
        dialogLogout.show(childFragmentManager,"")
    }

    private fun goToCameraFragment(location: Location?) {

    }
}// Required empty public constructor
