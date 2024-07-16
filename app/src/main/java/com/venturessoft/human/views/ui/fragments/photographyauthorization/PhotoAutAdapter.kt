package com.venturessoft.human.humanrhdapp.ui.home.ui.fragments.controlDeAusentismos.reportesEstacionesLibres.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.venturessoft.human.R
import com.venturessoft.human.databinding.ListItemEmployeePhotoBinding
import com.venturessoft.human.humanrhdapp.core.ViewHolderGeneral
import com.venturessoft.human.models.Response.LstFotoLocalItem
import com.venturessoft.human.views.ui.fragments.Employe.EditEmployeeFragment
import com.venturessoft.human.views.ui.fragments.photographyauthorization.ModelPhoto

class PhotoAutAdapter(
    private val item: ArrayList<LstFotoLocalItem>,
    private val itemClickListener: OnClickListener,
) : RecyclerView.Adapter<ViewHolderGeneral<*>>() {

    interface OnClickListener {
        fun onClick(lstFotoLocalItem: LstFotoLocalItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGeneral<*> {
        val itemBinding = ListItemEmployeePhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val itemholder = ViewHolder(itemBinding, parent.context)
        itemBinding.root.setOnClickListener {
            val position = itemholder.bindingAdapterPosition.takeIf { it != DiffUtil.DiffResult.NO_POSITION } ?: return@setOnClickListener
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
        val binding: ListItemEmployeePhotoBinding,
        val context: Context
    ) : ViewHolderGeneral<LstFotoLocalItem>(binding.root) {
        @SuppressLint("SetTextI18n")
        override fun bind(item: LstFotoLocalItem) {
            val numero = item.idEmpleado.toString().toLong()
            binding.listName.text = item.nombreEmpleado
            binding.listNumEmpl.text = numero.toString()

            if (!item.fotoNueva.isNullOrEmpty()) {
                try {
                    if (item.fotoNueva!!.contains("File not foundjava", ignoreCase = true) || item.fotoNueva!!.isEmpty()) {
                        binding.listImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.avartar_))
                    } else {
                        val imagenBase64 = Base64.decode(item.fotoNueva, Base64.DEFAULT)
                        val imagenconverBitmap = BitmapFactory.decodeByteArray(imagenBase64, 0, imagenBase64.size)
                        binding.listImage.setImageBitmap(imagenconverBitmap)
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }

        }
    }
}