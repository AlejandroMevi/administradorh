package com.venturessoft.human.views.ui.fragments.AuditHistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentViewImageBinding
import com.venturessoft.human.utils.Utilities.Companion.base64StringToBitmap

class ViewImageFragment(private val photo: String? = null) : DialogFragment() {

    private lateinit var binding: FragmentViewImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        toolbar()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
        }
    }
    private fun toolbar() {
        with(binding) {
            btnBack.setOnClickListener { dismiss() }
        }
    }

    private fun loadData() {
        if (photo != null) {
            val photoBitmap = base64StringToBitmap(photo)
            binding.idImg.setImageBitmap(photoBitmap)
        }

    }
}