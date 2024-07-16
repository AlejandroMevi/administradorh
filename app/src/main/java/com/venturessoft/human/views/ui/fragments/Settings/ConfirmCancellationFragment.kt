package com.venturessoft.human.views.ui.fragments.Settings

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentConfirmCancellationBinding
import com.venturessoft.human.models.Response.DeleteAcountResponse
import com.venturessoft.human.models.request.DeleteAccountRequest
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.LoginActivity
import com.venturessoft.human.views.ui.viewModels.DeleteAcountViewModel

class ConfirmCancellationFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentConfirmCancellationBinding
    private var deleteAcountViewModel = DeleteAcountViewModel()
    private var deleteAcountResponse = DeleteAcountResponse()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmCancellationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val dialog2 = dialog as BottomSheetDialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            dialog2.window?.setBackgroundDrawable(ColorDrawable(requireContext().getColor(R.color.bottom_sheet_back)))
        }
        dialog2.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog2.behavior.isDraggable = false
        return dialog2
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObservers()
        loadButtons()
        binding.btnAcept.isClickable = false
    }
    private fun loadButtons(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.textView15.text = Html.fromHtml(getString(R.string.bottom_sheet_delete_text), Html.FROM_HTML_MODE_LEGACY)
        } else {
            binding.textView15.text = Html.fromHtml(getString(R.string.bottom_sheet_delete_text))
        }
        binding.checkTxt.setOnClickListener {
            binding.check.isChecked = !binding.check.isChecked
        }
        binding.check.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                binding.btnAcept.isClickable = true
                binding.linearTxt.isVisible = true
                binding.btnAcept.setBackgroundResource(R.drawable.button_background_mturqoise)
            }else{
                binding.btnAcept.isClickable = false
                binding.linearTxt.isVisible = false
                binding.btnAcept.setBackgroundResource(R.drawable.button_background_blue)
            }
        }
        binding.btnAcept.setOnClickListener {
            loadServiceDelete()
        }
        binding.btnContinue.setOnClickListener {
            dismiss()
        }
    }
    private fun loadServiceDelete(){
        val request = DeleteAccountRequest(User.email, "B")
        deleteAcountViewModel.getDeleteAcountServices(request)
    }
    private fun addObservers(){
        deleteAcountViewModel.deleteAcountResponseMutableData.observe(viewLifecycleOwner) {
            deleteAcountResponse = it
            if (deleteAcountResponse.codigo == "ET000") {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.putExtra(Constants.FROM_LOG_OUT, true)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                ActivityCompat.finishAffinity(requireActivity())
            } else {
                Utilities.loadMessageError(
                    deleteAcountResponse.codigo.toString(),
                    requireActivity(),
                    childFragmentManager
                )
            }
        }
    }
}