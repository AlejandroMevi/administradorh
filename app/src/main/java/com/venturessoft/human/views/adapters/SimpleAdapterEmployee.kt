package com.venturessoft.human.views.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.google.android.material.materialswitch.MaterialSwitch
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.Employe.EmployeeListFragment
import java.util.*
import kotlin.collections.ArrayList

class SimpleAdapterEmployee(ctx: Context, private val items: MutableList<SearchModel>) :
    androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterEmployee.VH>() {
    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(EmployeeListFragment.imageModelArrayList)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rowsitemsmaintenance, parent, false)
        return VH(view as ViewGroup)

    }


    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(
            items[position].getNames(),
            items[position].getNumberr(),
            items[position].getApPaternoo(),
            items[position].getApMaternoo(),
            items[position].getFotoo(),
            items[position].getStatuss(),
            items[position].getIdd()
        )

    }

    override fun getItemCount(): Int = items.size

    fun addItem() {
        notifyItemInserted(items.size)
    }


    fun removeAtView(position: Int) {
        println("Id : :   :" + items[position].getIdd())
        EmployeeListFragment().eliminarEmpleado(items[position].getIdd())
        if (EmployeeListFragment.respuestaEliminaEmpleado == "ET000") {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }


    class VH(parent: ViewGroup) : androidx.recyclerview.widget.RecyclerView.ViewHolder(parent) {


        fun bind(
            name: String,
            number: String,
            apPaterno: String,
            apMaterno: String,
            foto: String,
            status: String,
            id: String
        ) = with(itemView) {
            val description = itemView.findViewById<TextView>(R.id.description)
            val employeenumber = itemView.findViewById<TextView>(R.id.employeenumber)
            val statusDescripcion = itemView.findViewById<MaterialSwitch>(R.id.statusDescripcion)
            val itemHeaderMaintenance =
                itemView.findViewById<LinearLayoutCompat>(R.id.itemHeaderMaintenance)
            description.text = "$apPaterno $apMaterno $name"
            employeenumber.text = number
            println("Status es $status")
            when (status) {
                "A" -> {
                    statusDescripcion.isChecked = true
                }

                else -> {
                    statusDescripcion.isChecked = false
                }
            }

            //Click sobre item
            itemHeaderMaintenance.setOnClickListener {
                //Habilita editar empleado
                EmployeeListFragment().editEmployee(id, number, name, apPaterno, apMaterno, foto)
            }

            statusDescripcion.setOnClickListener {

                EmployeeListFragment().updateMaintenance(number, status)
            }

        }
    }


    fun filter(charText: String) {
        var charText = charText
        charText = charText.lowercase(Locale.getDefault())
        EmployeeListFragment.imageModelArrayList.clear()
        if (charText.isEmpty()) {
            EmployeeListFragment.imageModelArrayList.addAll(arrayData)
        } else {
            //Si existe el texto dentro del arreglo
            println("ArrayList: $arrayData")
            for (wp in arrayData) {
                println("WP: + " + wp.getNames())
                if (wp.getNames().lowercase(Locale.getDefault()).contains(charText)) {
                    println(
                        "Contiene: " + wp.getNames().lowercase(Locale.getDefault())
                            .contains(charText)
                    )
                    EmployeeListFragment.imageModelArrayList.add(wp)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun refreshRecicler() {
        EmployeeListFragment.imageModelArrayList.clear()
        println("Refresca la vista: Mantenimiento de Empleados.")
        notifyDataSetChanged()

    }
}
