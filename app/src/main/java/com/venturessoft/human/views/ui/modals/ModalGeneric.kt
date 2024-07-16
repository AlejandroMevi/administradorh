package com.venturessoft.human.views.ui.modals

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentModalGenericBinding
import com.venturessoft.human.utils.Constants
import com.venturessoft.human.views.ui.activities.LoginActivity

class ModalGeneric() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentModalGenericBinding

    private var function: (() -> Unit)? = null

    companion object {
        fun newFunction(function: (() -> Unit)): ModalGeneric {
            val fragment = ModalGeneric()
            fragment.function = function
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalGenericBinding.inflate(inflater, container, false)
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
        with(binding) {
            btnAcept.setOnClickListener {
                function?.invoke()
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
            btnAceptOne.setOnClickListener {
                function?.invoke()
                dismiss()
            }
        }
    }

    fun updateBinding(
        twoButtons: Boolean,
        titleString: String,
        descriptionString: String? = null,
        titlebtnAcept: String,
        titlebtnCancel: String? = null,
    ) {
        when (twoButtons) {
            true -> {
                binding.btnAceptOne.isVisible = false
            }

            false -> {
                binding.twoButtons.isVisible = false
            }
        }
        with(binding) {
            title.text = titleString
            txtDescrip.isVisible = !descriptionString.isNullOrEmpty()
            txtDescrip.text = descriptionString ?: ""
            btnAcept.text = titlebtnAcept
            btnAceptOne.text = titlebtnAcept
            btnCancel.text = titlebtnCancel ?: ""
        }
    }

}