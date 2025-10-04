package com.example.gerenciadorcompras

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gerenciadorcompras.adapters.CategoriaAdapter
import com.example.gerenciadorcompras.adapters.UnidadeAdapter
import com.example.gerenciadorcompras.databinding.ActivityCriarItemBinding
import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.singletons.AppContainer.itemService
import com.example.gerenciadorcompras.singletons.AppContainer.userService
import com.example.gerenciadorcompras.viewmodels.CriarItemViewModel

class CriarItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCriarItemBinding
    private lateinit var viewModel: CriarItemViewModel
    private lateinit var itemCategoria: ItemCategoria
    private lateinit var itemUnidade: UnidadeItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCriarItemBinding.inflate(layoutInflater)

        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        setupSpinnerCategoria()

        setupSpinnerUnidade()

        viewModel = CriarItemViewModel(itemService)

        binding.buttonCriar.setOnClickListener {
            viewModel.criarItem(
                userService.getUserLogado()!!,
                binding.editTextNome.text.toString(),
                itemCategoria,
                binding.editTextQtd.text.toString(),
                itemUnidade,
                intent.getIntExtra("idLista", 0)
            )
        }

        viewModel.result.observe(this) { result ->
            if (result.success) {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinnerCategoria() {
        val spinner = binding.spinnerCate
        val arrayAdapter = CategoriaAdapter(this, ItemCategoria.entries.toTypedArray())
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val itemSelecionado = ItemCategoria.entries[position]

                itemCategoria = itemSelecionado
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Não implementado
            }
        }
    }

    private fun setupSpinnerUnidade() {
        val spinner = binding.spinnerUn
        val arrayAdapter = UnidadeAdapter(this, UnidadeItem.entries.toTypedArray())
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val itemSelecionado = UnidadeItem.entries[position]

                itemUnidade = itemSelecionado
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não implementado
            }
        }
    }


}