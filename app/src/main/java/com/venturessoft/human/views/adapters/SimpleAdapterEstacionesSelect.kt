package com.venturessoft.human.views.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kyleduo.switchbutton.SwitchButton
import com.venturessoft.human.R
import com.venturessoft.human.models.SearchModel
import com.venturessoft.human.views.ui.fragments.Employe.RegisterEmployeeFragment
import com.venturessoft.human.views.ui.fragments.Stations.ListEstacionesFragment

class SimpleAdapterEstacionesSelect(ctx: Context, private val items: MutableList<SearchModel>) : androidx.recyclerview.widget.RecyclerView.Adapter<SimpleAdapterEstacionesSelect.VH>() {

    private val arrayData = ArrayList<SearchModel>()
    private val inflater: LayoutInflater
    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(RegisterEmployeeFragment.imageModelArrayList)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rowsitemselect, parent, false)
        return VH(view as ViewGroup)
    }    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getIdd().toInt(), items[position].getNames(), items[position].getStatuss())
    }
    override fun getItemCount(): Int = items.size

    fun addItem() {
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
            val idStationSelect = findViewById<TextView>(R.id.idStationSelect)
            val headerSelect =  findViewById<LinearLayout>(R.id.headerSelect)
            val checkDone = findViewById<ImageView>(R.id.checkDone)
            val statusDescripcion = findViewById<SwitchButton>(R.id.statusDescripcion)
            description.text = nameStation
            idStationSelect.text = idEstacion.toString()
            headerSelect.setOnClickListener {
                if (RegisterEmployeeFragment.itemsSeleccionados.contains(idEstacion)) {
                    checkDone.visibility = View.INVISIBLE
                    RegisterEmployeeFragment.itemsSeleccionados.remove(idEstacion)
                } else {
                    checkDone.visibility = View.VISIBLE
                    RegisterEmployeeFragment.itemsSeleccionados.add(idEstacion)
                }
            }
            statusDescripcion.isChecked = statusStation == "A"
            if (RegisterEmployeeFragment.itemsSeleccionados.contains(idEstacion)) {
                checkDone.visibility = View.VISIBLE
            } else {
                checkDone.visibility = View.INVISIBLE
            }
        }
    }
}
