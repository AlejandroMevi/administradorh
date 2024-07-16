package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textview.MaterialTextView
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.utils.User
import com.venturessoft.human.views.ui.fragments.Stations.ListEstacionesFragment
import java.util.*
import kotlin.collections.ArrayList

class SimpleAdapterEstaciones(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterEstaciones.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ListEstacionesFragment.imageModelArrayList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemsestaciones, parent, false)
        return VH(view as ViewGroup)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(
            items[position].getIdd().toInt(),
            items[position].getNames(),
            items[position].getStatuss()
        )
    }
    override fun getItemCount(): Int = items.size
    fun addItem() {
        notifyItemInserted(items.size)
    }
    fun removeAt(position: Int) {
        ListEstacionesFragment().eliminarEstacion(items[position].getIdd())
        if (ListEstacionesFragment.deleteResultStation == "ET000") {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {
        fun bind(idEstacion: Int, nameStation: String, statusStation: String) = with(itemView) {
            val description = itemView.findViewById<MaterialTextView>(R.id.description)
            val idStation = itemView.findViewById<MaterialTextView>(R.id.idStation)
            val statusDescripcion = itemView.findViewById<MaterialSwitch>(R.id.statusDescripcion)
            description.text = nameStation
            idStation.text = idEstacion.toString()
            if (User.tipoUsuario == 2 && User.estaciones == true) {
                statusDescripcion.isActivated = false
                statusDescripcion.isClickable = false
                statusDescripcion.isEnabled = false

            } else {
                statusDescripcion.isActivated = true
                statusDescripcion.isClickable = true
                statusDescripcion.isEnabled = true
            }
            statusDescripcion.isChecked = statusStation == "A"


            description.setOnClickListener {
                ListEstacionesFragment().editEstacion(idEstacion)
            }
            statusDescripcion.setOnClickListener {
                ListEstacionesFragment().updateEstacion(idEstacion, statusStation)
            }
        }

    }
    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        ListEstacionesFragment.imageModelArrayList.clear()
        if (charText.isEmpty()) {
            ListEstacionesFragment.imageModelArrayList.addAll(arrayData)
        } else {
            for (wp in arrayData) {
                if (wp.getNames().lowercase(Locale.getDefault()).contains(charText)) {
                    ListEstacionesFragment.imageModelArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }
    fun refreshStations() {
        ListEstacionesFragment.imageModelArrayList.clear()
        notifyDataSetChanged()
    }
}
