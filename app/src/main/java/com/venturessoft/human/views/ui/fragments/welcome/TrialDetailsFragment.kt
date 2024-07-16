package com.venturessoft.human.views.ui.fragments.welcome

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentTrialDetailsBinding
import com.venturessoft.human.utils.User

class TrialDetailsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTrialDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrialDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        val dialog2 = dialog as BottomSheetDialog
        dialog2.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog2.behavior.isDraggable = false
        return dialog2
    }
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var totalCoste = User.maximoEmpleados.toDouble() * User.costoEmpleado
        totalCoste = "%.2f".format(totalCoste).replace(",", ".").toDouble()
        val price = "\$" + "%.2f".format(totalCoste).replace(",", ".")+" MXN"
        val stringData = "Después, el costo será de <b><font color=#0094AA>" + price + "</font></b> por tu plantilla de <b><font color=#0094AA>" + User.maximoEmpleados + " colaboradores</font></b>"
        binding.textViewDetails.text = Html.fromHtml(
            stringData
        )
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}