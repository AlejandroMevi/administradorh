package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportesEstacionesLibres.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ListItemEstacionesLibresBinding
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.models.Response.EstacionesEmpItem

class EstacionesLibresAdapter(
    private val item: ArrayList<EstacionesEmpItem>,
    private val itemClickListener: OnClickListener,
) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {

    interface OnClickListener {
        fun onClick(estacionesEmpItem: EstacionesEmpItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ListItemEstacionesLibresBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        val itemholder = ViewHolder(itemBinding, parent.context)
        itemBinding.root.setOnClickListener {
            val position =
                itemholder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION }
                    ?: return@setOnClickListener
            itemClickListener.onClick(item[position])
        }
        return itemholder
    }

    override fun onBindViewHolder(holder: ViewHolderGeneral<*>, position: Int) {
        val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_in)
        animation.duration = 500
        holder.itemView.startAnimation(animation)
        when (holder) {
            is ViewHolder -> holder.bind(item[position])
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int = item.size
    private inner class ViewHolder(
        val binding: ListItemEstacionesLibresBinding,
        val context: Context
    ) : ViewHolderGeneral<EstacionesEmpItem>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: EstacionesEmpItem) {
            val numero = item.numEmp.toString().toLong()
            binding.name.text = item.nombreEmp
            binding.numEmployee.text = numero.toString()
            binding.date.text =
                "${item.fechaChecada} ${context.getString(R.string.a_las)} ${item.horaChecada} "
            binding.bssid.text = item.estacion

            if (item.tipo == "E") {
                binding.login.isVisible = true
                binding.logout.isVisible = false
            } else {
                binding.logout.isVisible = true
                binding.login.isVisible = false
            }
        }
    }
}