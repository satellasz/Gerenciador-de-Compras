package com.example.gerenciadorcompras.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadorcompras.R
import com.example.gerenciadorcompras.databinding.ItemBinding
import com.example.gerenciadorcompras.models.Item

class ItemAdapter(
    private val onItemClick: (Item) -> Unit,
    private val onCheckBoxClick: (Item, Boolean) -> Unit
) :
    ListAdapter<Item, ItemAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        val context = holder.itemView.context

        with(holder.binding) {
            txtNome.text = item.nome
            txtQuantidade.text = context.getString(
                R.string.quantidade_format,
                item.quantidade,
                item.unidade.unidade
            )
            checkBox.isChecked = item.marcado
            imgItem.setImageResource(item.categoria.drawableRes)

            root.setOnClickListener {
                onItemClick(item)
            }

            checkBox.setOnClickListener {
                onCheckBoxClick(item, checkBox.isChecked)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }
}