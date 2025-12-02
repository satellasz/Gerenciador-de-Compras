package com.example.gerenciadorcompras.datasource.item

import com.example.gerenciadorcompras.enums.ItemCategoria
import com.example.gerenciadorcompras.enums.UnidadeItem
import com.example.gerenciadorcompras.models.Item
import com.example.gerenciadorcompras.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.math.absoluteValue

class ItemFireBaseDataSource(
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ItemDataSource {
    private val collectionItens = firestore.collection("itens")

    override suspend fun adicionarItem(
        id: Int,
        user: User,
        nome: String,
        categoria: ItemCategoria,
        quantidade: Int,
        unidade: UnidadeItem,
        idLista: Int
    ): Item? {
        val docRef = collectionItens.document()
        val idItem = docRef.id.hashCode().absoluteValue

        val item = Item(
            id = idItem,
            nome = nome,
            quantidade = quantidade,
            unidade = unidade,
            categoria = categoria,
            marcado = false,
            idLista = idLista,
            user = user
        )

        collectionItens.document(docRef.id)
            .set(item)
            .await()

        return item
    }

    override suspend fun deleteItem(item: Item) {
        val snap = collectionItens
            .whereEqualTo("id", item.id)
            .get()
            .await()

        val doc = snap.documents.firstOrNull()
            ?: throw Exception("Item não encontrado")

        val docRef = doc.reference

        docRef.delete().await()
    }

    override suspend fun updateItem(item: Item) {
        val snap = collectionItens
            .whereEqualTo("id", item.id)
            .get()
            .await()

        val doc = snap.documents.firstOrNull()
            ?: throw Exception("Item não encontrado")

        val docRef = doc.reference

        docRef.set(item).await()
    }

    override suspend fun encontrarItem(nome: String, idLista: Int): Item? {
        val snap = collectionItens
            .whereEqualTo("nome", nome)
            .whereEqualTo("idLista", idLista)
            .get()
            .await()

        return snap.documents.firstOrNull()?.toObject(Item::class.java)
    }

    override suspend fun encontrarItem(idItem: Int, idLista: Int): Item? {
        val snap = collectionItens
            .whereEqualTo("id", idItem)
            .whereEqualTo("idLista", idLista)
            .get()
            .await()

        return snap.documents.firstOrNull()?.toObject(Item::class.java)
    }

    override suspend fun getItens(idLista: Int): List<Item> {
        return collectionItens
            .whereEqualTo("idLista", idLista)
            .get()
            .await()
            .toObjects(Item::class.java)
    }
}
