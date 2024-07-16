package com.venturessoft.human.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.models.BeaconListModel
import com.venturessoft.human.models.ListAuditHistoryModel
import com.venturessoft.human.views.ui.fragments.AuditHistory.ListAudtHistoryFragment

class AdapterBeaconList(ctx: Context, private val items: MutableList<BeaconListModel>) : RecyclerView.Adapter<AdapterBeaconList.VH>() {
    private val arrayData = ArrayList<ListAuditHistoryModel>()
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(ctx)
        this.arrayData.addAll(ListAudtHistoryFragment.listAuditHistoryModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position].getNames(), items[position].getUuids())
    }

    override fun getItemCount(): Int = items.size

    class VH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_beacon_list, parent, false)) {
        fun bind(name: String, uuid: String) = with(itemView) {
            val txtNameBeacon:TextView = this.findViewById(R.id.txtBeaconName) as TextView
            val txtUUIDBeacon:TextView = this.findViewById(R.id.idBeacon) as TextView
            txtNameBeacon.text = name
            txtUUIDBeacon.text = uuid
        }
    }
}
