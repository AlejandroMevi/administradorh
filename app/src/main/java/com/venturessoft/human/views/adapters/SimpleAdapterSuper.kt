package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.materialswitch.MaterialSwitch
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.Supervisor.ListaSupervisorFragment
import java.util.*
import kotlin.collections.ArrayList

class SimpleAdapterSuper(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterSuper.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ListaSupervisorFragment.imageModelArrayList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemssuper, parent, false)
        return VH(view as ViewGroup)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getNames(),items[position].getApPaternoo(), items[position].getApMaternoo(), items[position].getStatuss(), items[position].getIdd())
    }
    override fun getItemCount(): Int = items.size
    fun addItem() {
        notifyItemInserted(items.size)
    }
    fun removeAt(position: Int) {
        ListaSupervisorFragment().eliminarEmpleado(items[position].getIdd())
        if (ListaSupervisorFragment.resultDeleteSuper == "ET000"){
        items.removeAt(position)
        notifyItemRemoved(position)
        }
    }
    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {
        fun bind(name: String,apPaterno: String, apMaterno: String, status: String, id: String) = with(itemView) {
            val description = findViewById<TextView>(R.id.description)
            val statusDescripcion = findViewById<MaterialSwitch>(R.id.statusDescripcion)
            val itemHeaderSuper = findViewById<LinearLayout>(R.id.itemHeaderSuper)
            description.text = "$apPaterno $apMaterno $name "
            statusDescripcion.isChecked = status == "A"
            itemHeaderSuper.setOnClickListener {
                ListaSupervisorFragment().editSuper(id)
            }
            statusDescripcion.setOnClickListener {
                if (status == "A") {
                    ListaSupervisorFragment().changeStatus(id, "B")
                } else {
                    ListaSupervisorFragment().changeStatus(id, "A")
                }
            }
        }
    }
    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        ListaSupervisorFragment.imageModelArrayList.clear()
        if (charText.isEmpty()) {
            ListaSupervisorFragment.imageModelArrayList.addAll(arrayData)
        } else {
            for (wp in arrayData) {
                if (wp.getNames().lowercase(Locale.getDefault()).contains(charText)) {
                    ListaSupervisorFragment.imageModelArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }
    fun refreshRecicler(){
        ListaSupervisorFragment.imageModelArrayList.clear()
        notifyDataSetChanged()
    }
}
