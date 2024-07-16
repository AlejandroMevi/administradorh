package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.ReportFragment.ReporByEmployee

class AdapterReportEmployee(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterReportEmployee.VH>()   {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ReporByEmployee.imageModelArrayList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemsreportemployee, parent, false)
        return VH(view as ViewGroup)

    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getNumberr(), items[position].getNames(), items[position].getApPaternoo(), items[position].getApMaternoo(),items[position].getFechaa(), items[position].getHoraEntradaa(), items[position].getHoraSalidaa(), items[position].getIdd(),items[position].getNameStationn())
    }

    override fun getItemCount(): Int = items.size

    fun addItem() {
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(
        parent) {

        private val dateText =  itemView.findViewById<TextView>(R.id.dateText)
        private val hourInReport =  itemView.findViewById<TextView>(R.id.hourInReport)
        private val hourOutReport =  itemView.findViewById<TextView>(R.id.hourOutReport)
        private val stationText =  itemView.findViewById<TextView>(R.id.stationText)
        fun bind(numEmp: String, name: String, apPaterno: String, apMaterno: String, fecha: String, horaEntrada: String, horaSalida: String, id: String, nameStation: String) = with(itemView) {

            dateText.text =fecha
            hourInReport.text =horaEntrada
            hourOutReport.text = horaSalida
            stationText.text =nameStation

        }
    }
}