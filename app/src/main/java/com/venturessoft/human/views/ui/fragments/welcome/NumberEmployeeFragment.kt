package com.venturessoft.human.views.ui.fragments.welcome

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport.Session.User
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentNumberEmployeeBinding
import com.venturessoft.human.databinding.FragmentUpdateEnterpriceBinding
import com.venturessoft.human.views.ui.activities.WayToPayActivity.Companion.employeNumber

class NumberEmployeeFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNumberEmployeeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNumberEmployeeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val dialog2 = dialog as BottomSheetDialog
        dialog2.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        return dialog2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val costXempl = com.venturessoft.human.utils.User.costoEmpleado.toString()
        val stringData = getString(R.string.number_employee_frag_costo, costXempl)
        binding.text3.text = Html.fromHtml(stringData)

        employeNumber.observe(viewLifecycleOwner){
            binding.employeNumbers.setText(it.toString())
        }

        binding.btnAccept.setOnClickListener {
            employeNumber.value =  binding.employeNumbers.text.toString().toInt()
            dismiss()
        }
    }
}