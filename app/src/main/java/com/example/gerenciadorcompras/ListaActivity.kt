package com.example.gerenciadorcompras

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gerenciadorcompras.adapters.ListaAdapter
import com.example.gerenciadorcompras.databinding.ActivityListaBinding
import com.example.gerenciadorcompras.singletons.AppContainer.listaService
import com.example.gerenciadorcompras.singletons.AppContainer.loginService
import com.example.gerenciadorcompras.singletons.AppContainer.userService

class ListaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var binding: ActivityListaBinding
    private lateinit var adapter: ListaAdapter

    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.hide()

        onBackPressedDispatcher.addCallback(this) {
            voltarTela()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val listas = listaService.getListasPorUsuario(userService.getUserLogado()!!)

        adapter = ListaAdapter { lista ->
            val intent = Intent(this@ListaActivity, ListaItemActivity::class.java)
            intent.putExtra("tituloLista", lista.titulo)
            intent.putExtra("idLista", lista.id)
            launcher.launch(intent)
        }

        recyclerView.adapter = adapter
        adapter.submitList(listas)

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this@ListaActivity, CriarListaActivity::class.java)
            launcher.launch(intent)
        }

        binding.imageButton.setOnClickListener {
            voltarTela()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val novasListas =
                        listaService.getListasPorUsuario(userService.getUserLogado()!!, it)
                    adapter.submitList(novasListas)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val novasListas =
                        listaService.getListasPorUsuario(userService.getUserLogado()!!, it)
                    adapter.submitList(novasListas)
                }
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        val novasListas = listaService.getListasPorUsuario(userService.getUserLogado()!!)
        adapter.submitList(novasListas)
    }

    private fun voltarTela() {
        loginService.logout()
        val intent = Intent(this@ListaActivity, MainActivity::class.java)
        launcher.launch(intent)
    }
}