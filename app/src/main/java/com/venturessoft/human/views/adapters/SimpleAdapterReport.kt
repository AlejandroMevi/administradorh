package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReportFragment

class SimpleAdapterReport(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterReport.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ReportFragment.imageModelArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemsreport, parent, false)
        return VH(view as ViewGroup)

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getNumberr(), items[position].getNames(), items[position].getApPaternoo(), items[position].getApMaternoo(), items[position].getHoraEntradaa(), items[position].getHoraSalidaa(), items[position].getIdd())
    }

    override fun getItemCount(): Int = items.size

    fun addItem() {
        notifyItemInserted(items.size)
    }

    fun removeAt() {
        items.clear()
        notifyDataSetChanged()
    }

    fun deleteAllData() {
        items.clear()
        notifyDataSetChanged()
    }

    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {
        fun bind(numEmp: String, name: String, apPaterno: String, apMaterno: String, horaEntrada: String, horaSalida: String, id: String) = with(itemView) {
            val nEmployee = findViewById<TextView>(R.id.nEmployee)
            val nameEmployee = findViewById<TextView>(R.id.nameEmployee)
            val hourIn = findViewById<TextView>(R.id.hourIn)
            val hourOut = findViewById<TextView>(R.id.hourOut)
            nEmployee.text = numEmp
            nameEmployee.text = "$apPaterno  $apMaterno $name"
            hourIn.text = horaEntrada
            hourOut.text = horaSalida
        }
    }
}
