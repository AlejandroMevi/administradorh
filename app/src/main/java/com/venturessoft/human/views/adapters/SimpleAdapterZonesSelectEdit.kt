package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.Supervisor.EditSupervisorFragment

class SimpleAdapterZonesSelectEdit(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterZonesSelectEdit.VH>() {

    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(EditSupervisorFragment.imageModelArrayList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemselect, parent, false)
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
        notifyItemRemoved(position)
    }
    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {
        fun bind(idZone: String, nameZone: String, statusZone: String) = with(itemView) {
            val headerSelect = findViewById<LinearLayout>(R.id.headerSelect)
            val checkDone = findViewById<ImageView>(R.id.checkDone)
            val description = findViewById<TextView>(R.id.description)
            headerSelect.setOnClickListener {
                if (EditSupervisorFragment.itemsSeleccionados.contains(idZone.toInt())) {
                    checkDone.visibility = View.INVISIBLE
                    EditSupervisorFragment.itemsSeleccionados.remove(idZone.toInt())
                    EditSupervisorFragment.itemsDeseleccionados.add(idZone.toInt())
                } else {
                    checkDone.visibility = View.VISIBLE
                    EditSupervisorFragment.itemsSeleccionados.add(idZone.toInt())
                    EditSupervisorFragment.itemsDeseleccionados.remove(idZone.toInt())
                }
            }
            description.text = nameZone
            if (EditSupervisorFragment.itemsSeleccionados.contains(idZone.toInt())){
                checkDone.visibility = View.VISIBLE
            }else{
                checkDone.visibility = View.INVISIBLE
            }
        }
    }
}

