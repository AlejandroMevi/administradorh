package com.venturessoft.human.views.ui.modals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentModalLogOutBinding
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.views.ui.activities.LoginActivity

class ModalLogOut : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentModalLogOutBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalLogOutBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            dialog.window!!.setBackgroundDrawableResource(R.color.colorBlue7)
        }
    }


    private fun initView() {
        with(binding){
            btnLogOut.setOnClickListener { logOut() }
            btnCancel.setOnClickListener { dismiss() }
        }
    }
    private fun logOut(){
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra(Constants.FROM_LOG_OUT, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        requireActivity().finishAffinity()
    }

}