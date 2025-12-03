package com.example.gerenciadorcompras.datasource.user

import com.example.gerenciadorcompras.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlin.math.absoluteValue

class UserFireBaseDataSource(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : UserDataSource {
    private val collection = firestore.collection("users")

    override suspend fun adicionarUser(
        id: Int,
        username: String,
        email: String,
        password: String
    ): User? {
        val user: User
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser =
            authResult.user ?: throw Exception("Usuário não encontrado na criação")

        try {
            user = User(
                id = firebaseUser.uid.hashCode().absoluteValue,
                username = username,
                email = email,
                password = password
            )

            collection
                .document(firebaseUser.uid)
                .set(user)
                .await()

            user
        } catch (e: Exception) {
            firebaseUser.delete().await()
            throw e
        }
        return user
    }

    override suspend fun encontrarUser(
        email: String,
        password: String
    ): User? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val uid = result.user?.uid ?: return null

        val userDoc = collection.document(uid).get().await()
        return userDoc.toObject(User::class.java)
    }

    override suspend fun encontrarUserPorEmail(email: String): User? {
        val querySnapshot = collection
            .whereEqualTo("email", email)
            .get()
            .await()

        return querySnapshot.documents.first().toObject(User::class.java)
    }

    override suspend fun getUserLogado(): User? {
        val firebaseUser = auth.currentUser ?: throw Exception("Usuário não logado")

        val querySnapshot = collection
            .whereEqualTo("id", firebaseUser.uid.hashCode().absoluteValue)
            .get()
            .await()

        return querySnapshot.documents.first().toObject(User::class.java)
    }

    override suspend fun setUserLogado(user: User?) {
        if (user == null) {
            auth.signOut()
            return
        }

        val authResult = auth.signInWithEmailAndPassword(user.email, user.password).await()
        authResult.user ?: throw Exception("Usuário não encontrado")
    }

    override suspend fun recuperarConta(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }
}
