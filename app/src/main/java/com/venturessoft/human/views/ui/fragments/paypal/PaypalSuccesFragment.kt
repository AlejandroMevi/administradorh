package com.venturessoft.human.views.ui.fragments.paypal

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentEnterpriceDetailsBinding
import com.venturessoft.human.databinding.FragmentPaypalSuccesBinding
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.views.ui.activities.LoginActivity
import com.venturessoft.human.views.ui.activities.WayToPayActivity

class PaypalSuccesFragment(val folio:String) :  BottomSheetDialogFragment(){

    private lateinit var binding: FragmentPaypalSuccesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaypalSuccesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val dialog2 = dialog as BottomSheetDialog
        dialog2.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog2.behavior.isDraggable = false
        return dialog2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textFolio.text = folio
        binding.btnAcept.setOnClickListener {
            dismiss()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.putExtra(Constants.FROM_LOG_OUT, true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            requireActivity().finishAffinity()
        }
    }
}