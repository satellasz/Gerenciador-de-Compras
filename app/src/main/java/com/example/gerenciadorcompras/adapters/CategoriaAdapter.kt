package com.example.gerenciadorcompras.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.gerenciadorcompras.R
import com.example.gerenciadorcompras.databinding.ItemSpinnerBinding
import com.example.gerenciadorcompras.enums.ItemCategoria

class CategoriaAdapter(
    context: Context,
    private val items: Array<ItemCategoria>
) : ArrayAdapter<ItemCategoria>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent, false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent, true)
    }

    private fun createView(
        position: Int,
        convertView: View?,
        parent: ViewGroup,
        isDropdown: Boolean
    ): View {
        val binding: ItemSpinnerBinding = if (convertView == null) {
            ItemSpinnerBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemSpinnerBinding.bind(convertView)
        }

        val item = items[position]
        binding.itemText.text = item.nome
        binding.itemIcon.setImageResource(item.drawableRes)

        if (isDropdown) {
            binding.itemText.setTextColor(context.getColor(R.color.orange))
        } else {
            binding.itemText.setTextColor(context.getColor(R.color.black))
        }

        return binding.root
    }
}