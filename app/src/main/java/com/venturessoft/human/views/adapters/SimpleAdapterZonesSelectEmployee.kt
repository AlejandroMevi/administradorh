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
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment

class SimpleAdapterZonesSelectEmployee(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterZonesSelectEmployee.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(RegisterEmployeeFragment.imageModelArrayListEmployee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.rowsitemselect, parent, false)
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
            //Cambiar de color al seleccionar agregar elemento de arreglo y posicion  habilitar imagen
            headerSelect.setOnClickListener {

                if (RegisterEmployeeFragment.itemsSeleccionadosE.contains(idZone.toInt())) {
                    checkDone.visibility = View.INVISIBLE
                    RegisterEmployeeFragment.itemsSeleccionadosE.remove(idZone.toInt())
                } else {
                    checkDone.visibility = View.VISIBLE
                    RegisterEmployeeFragment.itemsSeleccionadosE.add(idZone.toInt())
                    RegisterEmployeeFragment.nameZone = nameZone
                }
            }
            description.text = nameZone


            if (RegisterEmployeeFragment.itemsSeleccionadosE.contains(idZone.toInt())){
                checkDone.visibility = View.VISIBLE
                RegisterEmployeeFragment.nameZone = nameZone
            }else{
                checkDone.visibility = View.INVISIBLE
            }
        }
    }

}

