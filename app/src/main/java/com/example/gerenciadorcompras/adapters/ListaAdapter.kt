package com.example.gerenciadorcompras.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadorcompras.R
import com.example.gerenciadorcompras.models.Lista
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class ListaAdapter : ListAdapter<Lista, ListaAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: ImageView = itemView.findViewById(R.id.imgItem)
        val txtItem: TextView = itemView.findViewById(R.id.txtItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.txtItem.text = item.titulo
        holder.imgItem.setImageURI(item.logoUri.toUri())
    }

    class DiffCallback : DiffUtil.ItemCallback<Lista>() {
        override fun areItemsTheSame(oldItem: Lista, newItem: Lista) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Lista, newItem: Lista) = oldItem == newItem
    }
}