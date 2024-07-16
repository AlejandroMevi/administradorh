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
import com.venturessoft.human.views.ui.fragments.Employe.EditEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Stations.ListEstacionesFragment

class SimpleAdapterEstacionesSelectEdit(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterEstacionesSelectEdit.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(EditEmployeeFragment.imageModelArrayList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemselect, parent, false)
        return VH(view as ViewGroup)

    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getIdd().toInt(), items[position].getNames(), items[position].getStatuss())
    }

    override fun getItemCount(): Int = items.size

    fun addItem() {
        //  items.add(0, date)
        notifyItemInserted(items.size)
    }

    fun removeAt(position: Int) {
        ListEstacionesFragment().eliminarEstacion(position.toString())
        items.removeAt(position)


        notifyItemRemoved(position)
    }

    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {
        fun bind(idEstacion: Int, nameStation: String, statusStation: String) = with(itemView) {
            val description = findViewById<TextView>(R.id.description)
            val headerSelect = findViewById<LinearLayout>(R.id.headerSelect)
            val checkDone = findViewById<ImageView>(R.id.checkDone)
            description.text = nameStation
            headerSelect.setOnClickListener {
                if (EditEmployeeFragment.itemsSeleccionados.contains(idEstacion)) {
                    checkDone.visibility = View.INVISIBLE
                    EditEmployeeFragment.itemsSeleccionados.remove(idEstacion)
                    EditEmployeeFragment.itemsDeseleccionados.add(idEstacion)
                } else {
                    checkDone.visibility = View.VISIBLE
                    EditEmployeeFragment.itemsSeleccionados.add(idEstacion)
                    EditEmployeeFragment.itemsDeseleccionados.remove(idEstacion)

                }
            }

            if (EditEmployeeFragment.itemsSeleccionados.contains(idEstacion)) {
                checkDone.visibility = View.VISIBLE
            } else {
                checkDone.visibility = View.INVISIBLE
            }
        }

    }

}
