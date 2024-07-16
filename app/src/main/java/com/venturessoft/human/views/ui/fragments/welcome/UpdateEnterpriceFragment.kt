package com.venturessoft.human.views.ui.fragments.welcome

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.venturessoft.human.R
import com.venturessoft.human.databinding.FragmentTrialDetailsBinding
import com.venturessoft.human.databinding.FragmentUpdateEnterpriceBinding
import com.venturessoft.human.utils.User
import com.venturessoft.human.utils.Utilities
import com.venturessoft.human.views.ui.activities.WayToPayActivity

class UpdateEnterpriceFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentUpdateEnterpriceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateEnterpriceBinding.inflate(inflater, container, false)
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

        val stringData1 = getString(R.string.update_enterprice_agregar_cola)
        binding.textView1.text = Html.fromHtml(
            stringData1
        )

        val stringData2 = getString(R.string.update_enterprice_costo)
        binding.textindicator7.text = Html.fromHtml(
            stringData2
        )

        val maxEmpl = User.maximoEmpleados.toString()
        val stringData3 = getString(R.string.update_enterprice_plantilla_de, maxEmpl)
        binding.textindicator9.text = Html.fromHtml(
            stringData3
        )

        var totalCoste = User.maximoEmpleados.toDouble() * User.costoEmpleado
        totalCoste = "%.2f".format(totalCoste).replace(",", ".").toDouble()
        val price = "\$" + "%.2f".format(totalCoste).replace(",", ".") + " MXN"
        binding.textindicator8.text = "$price ${getString(R.string.enterprice_month)}"

        val dateFormat =
            if (User.fechaVigencia.isNullOrEmpty() || User.fechaVigencia == "") Utilities.cambiarFormatoFecha(Utilities.dateExpiredFreeTrail())
            else Utilities.cambiarFormatoFecha(User.fechaVigencia)
        binding.textView2.text = getString(R.string.enterprice_expiracion, dateFormat)

        binding.imageView6.setOnClickListener {
            dismiss()
        }
        binding.btnAcept.setOnClickListener {
            dismiss()
            val intent = Intent(requireActivity(), WayToPayActivity::class.java)
            intent.putExtra("isPaypal", false)
            startActivity(intent)
        }
        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }
}