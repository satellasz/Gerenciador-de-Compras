package com.example.gerenciadorcompras.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.gerenciadorcompras.databinding.ItemListaBinding
import com.example.gerenciadorcompras.models.Lista

class ListaAdapter(private val onItemClick: (Lista) -> Unit) :
    ListAdapter<Lista, ListaAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemListaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            txtItem.text = item.titulo
            Glide.with(root)
                .load(item.logoUri.toUri())
                .centerCrop()
                .into(imgItem)

            root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Lista>() {
        override fun areItemsTheSame(oldItem: Lista, newItem: Lista) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Lista, newItem: Lista) = oldItem == newItem
    }
}