package com.example.gerenciadorcompras.datasource.lista

import com.example.gerenciadorcompras.models.Lista
import com.example.gerenciadorcompras.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.math.absoluteValue

class ListaFireBaseDataSource(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : ListaDataSource {
    private val collection = firestore.collection("listas")
    private val collectionItens = firestore.collection("itens")

    override suspend fun adicionarLista(
        id: Int,
        user: User,
        titulo: String,
        logoUri: String
    ): Lista? {
        val docRef = collection.document()
        val id = docRef.id

        val lista = Lista(
            id = id.hashCode().absoluteValue,
            titulo = titulo,
            logoUri = logoUri,
            user = user
        )

        collection.document(id)
            .set(lista)
            .await()

        return lista
    }

    override suspend fun deleteLista(lista: Lista) {
        val snap = collection
            .whereEqualTo("id", lista.id)
            .get()
            .await()

        val doc = snap.documents.firstOrNull()
            ?: throw Exception("Lista não encontrado")

        val docRef = doc.reference

        docRef.delete().await()
    }

    override suspend fun deleteItensLista(idLista: Int) {
        val docs = collectionItens
            .whereEqualTo("idLista", idLista)
            .get()
            .await()
            .documents

        if (docs.isEmpty()) return

        val batch = firestore.batch()

        docs.forEach { batch.delete(it.reference) }

        batch.commit().await()
    }

    override suspend fun updateLista(lista: Lista) {
        val snap = collection
            .whereEqualTo("id", lista.id)
            .get()
            .await()

        val doc = snap.documents.firstOrNull()
            ?: throw Exception("Lista não encontrado")

        val docRef = doc.reference

        docRef.set(lista).await()
    }

    override suspend fun encontrarLista(titulo: String): Lista? {
        val snap = collection
            .whereEqualTo("titulo", titulo)
            .get()
            .await()

        return snap.documents.firstOrNull()?.toObject(Lista::class.java)
    }

    override suspend fun encontrarLista(idLista: Int): Lista? {
        val snap = collection
            .whereEqualTo("id", idLista)
            .get()
            .await()

        return snap.documents.firstOrNull()?.toObject(Lista::class.java)
    }

    override suspend fun getListas(): List<Lista> {
        return collection.get()
            .await()
            .toObjects(Lista::class.java)

    }
}