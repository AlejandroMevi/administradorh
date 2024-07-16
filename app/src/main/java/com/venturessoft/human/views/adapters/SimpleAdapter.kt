package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textview.MaterialTextView
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.Zones.ListZonesFragment
import java.util.Locale

class SimpleAdapter(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapter.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ListZonesFragment.imageModelArrayList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitems, parent, false)
        return VH(view as ViewGroup)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getIdd(), items[position].getNames(), items[position].getStatuss())
    }
    override fun getItemCount(): Int = items.size
    fun addItem() {
        notifyItemInserted(items.size)
    }
    fun removeAt(position: Int) {
        ListZonesFragment().eliminarZona(items[position].getIdd())
        if (ListZonesFragment.eliminaZonaResponse.codigo == "ET000") {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {
        fun bind(idZone: String, nameZone: String, statusZone: String) = with(itemView) {
            val description =  itemView.findViewById<MaterialTextView>(R.id.description)
            val idZona = itemView.findViewById<MaterialTextView>(R.id.idZona)
            val statusDescripcion = itemView.findViewById<MaterialSwitch>(R.id.statusDescripcion)
            val llBase = itemView.findViewById<LinearLayoutCompat>(R.id.llBase)


            description.text = nameZone
            idZona.text = idZone
            statusDescripcion.isChecked = statusZone == "A"
            llBase.setOnClickListener {
                ListZonesFragment().editZone(idZone.toInt(), nameZone)
            }
            statusDescripcion.setOnClickListener {
                if (statusZone == "A") {
                    ListZonesFragment().editZoneStatuss(idZone.toInt(), "B", nameZone)
                } else {
                    ListZonesFragment().editZoneStatuss(idZone.toInt(), "A", nameZone)

                }
            }
        }
    }
    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        ListZonesFragment.imageModelArrayList.clear()
        if (charText.isEmpty()) {
            ListZonesFragment.imageModelArrayList.addAll(arrayData)
        } else {
            for (wp in arrayData) {
                if (wp.getNames().lowercase(Locale.getDefault()).contains(charText)) {
                    ListZonesFragment.imageModelArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }
    fun refreshRecicler() {
        ListZonesFragment.imageModelArrayList.clear()
        notifyDataSetChanged()
    }
}
