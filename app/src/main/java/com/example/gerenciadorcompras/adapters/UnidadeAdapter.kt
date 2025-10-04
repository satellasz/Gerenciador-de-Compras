package com.example.gerenciadorcompras.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.gerenciadorcompras.R
import com.example.gerenciadorcompras.databinding.ItemSpinnerSemIconeBinding
import com.example.gerenciadorcompras.enums.UnidadeItem

class UnidadeAdapter(
    context: Context,
    private val items: Array<UnidadeItem>
) : ArrayAdapter<UnidadeItem>(context, 0, items) {

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
        val binding: ItemSpinnerSemIconeBinding = if (convertView == null) {
            ItemSpinnerSemIconeBinding.inflate(LayoutInflater.from(context), parent, false)
        } else {
            ItemSpinnerSemIconeBinding.bind(convertView)
        }

        val item = items[position]
        binding.itemText.text = item.unidade

        if (isDropdown) {
            binding.itemText.setTextColor(context.getColor(R.color.orange))
        } else {
            binding.itemText.setTextColor(context.getColor(R.color.black))
        }

        return binding.root
    }
}