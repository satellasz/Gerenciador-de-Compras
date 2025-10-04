package com.example.gerenciadorcompras

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadorcompras.adapters.ItemAdapter
import com.example.gerenciadorcompras.databinding.ActivityListaItemBinding
import com.example.gerenciadorcompras.singletons.AppContainer.itemService
import com.example.gerenciadorcompras.singletons.AppContainer.loginService
import com.example.gerenciadorcompras.singletons.AppContainer.userService

class ListaItemActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityListaItemBinding

    private lateinit var adapter: ItemAdapter

    private var idLista: Int = 0

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaItemBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.hide()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val titulo =
            if (intent.getStringExtra("tituloLista") != null) intent.getStringExtra("tituloLista") else "Lista"

        binding.txtTitulo.text = titulo

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 1)

        idLista = intent.getIntExtra("idLista", 0)

        val itens = itemService.getItensListaPorUsuario(userService.getUserLogado()!!, idLista)

        adapter = ItemAdapter()
        recyclerView.adapter = adapter
        adapter.submitList(itens)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@ListaItemActivity, CriarItemActivity::class.java)
            intent.putExtra("idLista", idLista)
            launcher.launch(intent)
        }

        binding.imageButton.setOnClickListener {
            loginService.logout()
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val novasListas = itemService.getItensListaPorUsuario(
                        userService.getUserLogado()!!,
                        idLista,
                        it
                    )
                    adapter.submitList(novasListas)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val novasListas = itemService.getItensListaPorUsuario(
                        userService.getUserLogado()!!,
                        idLista,
                        it
                    )
                    adapter.submitList(novasListas)
                }
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val novasListas =
            itemService.getItensListaPorUsuario(userService.getUserLogado()!!, idLista)
        adapter.submitList(novasListas)
    }
}