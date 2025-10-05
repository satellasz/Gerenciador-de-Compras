package com.example.gerenciadorcompras

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.gerenciadorcompras.adapters.CategoriaAdapter
import com.example.gerenciadorcompras.adapters.UnidadeAdapter
import com.example.gerenciadorcompras.databinding.ActivityCriarItemBinding
import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.StatusResult
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.singletons.AppContainer.itemService
import com.example.gerenciadorcompras.singletons.AppContainer.listaService
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

        val idItem = intent.getIntExtra("idItem", 0)
        val idLista = intent.getIntExtra("idLista", 0)

        val itemSalvo = itemService.encontrarItem(idItem, idLista)

        viewModel = CriarItemViewModel(itemService)

        if (itemSalvo != null) {
            setupSpinnerCategoria(itemSalvo)

            setupSpinnerUnidade(itemSalvo)

            setupEditar(itemSalvo)
        } else {
            setupSpinnerCategoria(null)

            setupSpinnerUnidade(null)
        }

        binding.buttonCriar.setOnClickListener {
            if (itemSalvo != null) {
                viewModel.updateItem(
                    binding.editTextNome.text.toString(),
                    itemCategoria,
                    binding.editTextQtd.text.toString(),
                    itemUnidade,
                    itemSalvo.id,
                    idLista
                )
            } else {
                viewModel.criarItem(
                    userService.getUserLogado()!!,
                    binding.editTextNome.text.toString(),
                    itemCategoria,
                    binding.editTextQtd.text.toString(),
                    itemUnidade,
                    idLista
                )
            }
        }

        viewModel.result.observe(this) { result ->
            Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
            when (result.status) {
                StatusResult.SALVO -> {
                    finish()
                }

                StatusResult.EDITOU -> {
                    val intent = Intent(this@CriarItemActivity, ListaItemActivity::class.java)
                    val lista = listaService.encontrarLista(idLista)

                    if (lista != null) {
                        intent.putExtra("tituloLista", lista.titulo)
                        intent.putExtra("idLista", lista.id)
                    }

                    startActivity(intent)
                }

                else -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSpinnerCategoria(item: Item?) {
        val spinner = binding.spinnerCate
        val categorias = ItemCategoria.entries.toTypedArray()
        val arrayAdapter = CategoriaAdapter(this, categorias)
        spinner.adapter = arrayAdapter

        if (item != null) {
            val posicao = categorias.indexOf(item.categoria)
            if (posicao >= 0) {
                spinner.setSelection(posicao)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                itemCategoria = categorias[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                // Não implementado
            }
        }
    }

    private fun setupSpinnerUnidade(item: Item?) {
        val spinner = binding.spinnerUn
        val unidades = UnidadeItem.entries.toTypedArray()
        val arrayAdapter = UnidadeAdapter(this, unidades)
        spinner.adapter = arrayAdapter

        if (item != null) {
            val posicao = unidades.indexOf(item.unidade)
            if (posicao >= 0) {
                spinner.setSelection(posicao)
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                itemUnidade = unidades[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Não implementado
            }
        }
    }

    private fun setupEditar(item: Item) {
        binding.textCriarConta.text = this.getString(R.string.texto, "Editar Item")
        binding.buttonCriar.text = this.getString(R.string.texto, "Salvar")
        binding.buttonExcluir.isVisible = true
        binding.buttonExcluir.isClickable = true
        binding.buttonExcluir.isEnabled = true

        binding.editTextNome.setText(item.nome)
        binding.editTextQtd.setText(item.quantidade.toString())

        binding.buttonExcluir.setOnClickListener {
            viewModel.deletarItem(item.id, item.idLista)
        }
    }
}