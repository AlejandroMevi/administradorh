package com.venturessoft.human.views.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.venturessoft.human.R
import com.venturessoft.human.models.ListAuditHistoryModel
import com.venturessoft.human.views.ui.fragments.AuditHistory.ListAudtHistoryFragment

class AdapterListAuditHistory(ctx: Context, private val items: MutableList<ListAuditHistoryModel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<AdapterListAuditHistory.VH>() {
    private val arrayData = ArrayList<ListAuditHistoryModel>()
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ListAudtHistoryFragment.listAuditHistoryModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(
            items[position].getNumCia(),
            items[position].getNumEmp(),
            items[position].getIds(),
            items[position].getFechaChecadaa(),
            items[position].getTipoo(),
            items[position].getCodigoEtimee()
        )
    }

    override fun getItemCount(): Int = items.size

    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.layout_list_history, parent, false)
    ) {
        fun bind(
            numCiaa: Long,
            numEmp: Long,
            id: String,
            fechaChecadaa: String,
            tipo: String,
            codigoEtimee: String
        ) = with(itemView) {
            val dateandtime = this.findViewById(R.id.dateandtime) as TextView
            val employeeNumber = this.findViewById(R.id.employeeNumber) as TextView
            val typemov = this.findViewById(R.id.typemov) as TextView
            val statusmov = this.findViewById(R.id.statusMov) as TextView
            val codigoEtime = this.findViewById(R.id.codigoET) as TextView
            val numCia = this.findViewById(R.id.numCia) as TextView
            val idEmp = this.findViewById(R.id.idEmp) as TextView
            dateandtime.text = fechaChecadaa
            employeeNumber.text = numEmp.toString()
            numCia.text = numCiaa.toString()
            idEmp.text = id
            if (tipo == "E") {
                typemov.text = context.getString(R.string.checkin)
            } else {
                typemov.text = context.getString(R.string.checkout)
            }


            if (codigoEtimee == "ET000") {
                statusmov.text = context.getString(R.string.audit_history_successful)
                statusmov.setTextColor(Color.parseColor("#148835"))

            } else {
                statusmov.text = context.getString(R.string.audit_history_failed)
                statusmov.setTextColor(Color.parseColor("#ed180a"))

            }
            val contextoPaquete: String = context!!.packageName
            val identificadorMensaje =
                this.resources.getIdentifier(codigoEtimee, "string", contextoPaquete)
            if (identificadorMensaje > 0) {
                if (codigoEtimee == "ET000") {
                    codigoEtime.text = "ET000"
                } else {

                    codigoEtime.text = context.getString(identificadorMensaje)

                }
            } else {
                codigoEtime.text = codigoEtimee
            }
        }
    }

}
