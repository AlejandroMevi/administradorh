package com.venturessoft.human.views.ui.fragments.welcome

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentEnterpriceDetailsBinding
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.WayToPayActivity

class EnterpriceDetailsFragment(private val isPaypal: Boolean) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEnterpriceDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterpriceDetailsBinding.inflate(inflater, container, false)
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

        val stringData = getString(R.string.enterprice_details_costo)
        binding.textindicator7.text = Html.fromHtml(
            stringData
        )

        var totalCoste = User.maximoEmpleados.toDouble() * User.costoEmpleado
        totalCoste = "%.2f".format(totalCoste).replace(",", ".").toDouble()
        val price = "\$" + "%.2f".format(totalCoste).replace(",", ".")+" MXN"
        binding.textindicator8.text = "$price ${getString(R.string.enterprice_month)}"
        val txtMaxUsers = User.maximoEmpleados.toString()
        val stringData2 = getString(R.string.enterprice_details_plantilla, txtMaxUsers)
        binding.textindicator9.text = Html.fromHtml(
            stringData2
        )
        val dateFormat = if (User.fechaVigencia.isNullOrEmpty() || User.fechaVigencia == "") Utilities.cambiarFormatoFecha(Utilities.dateExpiredFreeTrail())
        else Utilities.cambiarFormatoFecha(User.fechaVigencia)
        binding.textView2.text = getString(R.string.enterprice_concluira, dateFormat)

        binding.imageView6.setOnClickListener {
            dismiss()
        }
        binding.btnAcept.setOnClickListener {
            dismiss()
            if (!isPaypal){
                val intent = Intent(requireActivity(), WayToPayActivity::class.java)
                intent.putExtra("isPaypal", false)
                startActivity(intent)
            }
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}