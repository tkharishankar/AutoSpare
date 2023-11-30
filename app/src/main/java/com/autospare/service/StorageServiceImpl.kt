package com.autospare.service

import androidx.core.net.toUri
import androidx.core.os.trace
import com.autospare.data.Order
import com.autospare.data.Product
import com.autospare.data.Status
import com.autospare.data.User
import com.autospare.service.state.CreateUserState
import com.autospare.service.state.GetUserState
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import java.io.File
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
) : StorageService {

    override val products: Flow<List<Product>>
        get() = firestore.collection(PRODUCT_COLLECTION)
            .dataObjects()

    override suspend fun getProduct(productId: String): Product? =
        firestore.collection(PRODUCT_COLLECTION).document(productId).get().await().toObject()

    override suspend fun save(product: Product): String = withContext(Dispatchers.IO) {
        trace(SAVE_PRODUCT_TRACE) {
            val storageReference = firebaseStorage.getReference("/products")
            val file = File(product.imageUrl)
            val ref = storageReference.child("/${Clock.System.now().epochSeconds}.jpg")
            val uploadTask = ref.putFile(file.toUri())
            await(uploadTask)
            val storageUrl = ref.downloadUrl.await().toString()
            val updatedProduct = product.copy(imageUrl = storageUrl)
            val documentReference =
                firestore.collection(PRODUCT_COLLECTION).add(updatedProduct).await()
            documentReference.id
        }
    }

    override suspend fun update(product: Product): Unit =
        trace(UPDATE_PRODUCT_TRACE) {
            firestore.collection(PRODUCT_COLLECTION).document(product.name).set(product).await()
        }

    override suspend fun delete(productId: String) {
        firestore.collection(PRODUCT_COLLECTION).document(productId).delete().await()
    }

    override suspend fun createOrder(
        userMailId: String,
        username: String,
        products: List<Product>,
    ): String =
        trace(CREATE_ORDER_TRACE) {
            val order = Order(
                userId = userMailId,
                username = username,
                orderId = Clock.System.now().epochSeconds.toString(),
                products = products.filter { it.isSelected }.map { it.productId },
                createdTimestamp = Clock.System.now().epochSeconds,
                status = Status.CREATED
            )
            val documentReference =
                firestore.collection(ORDER_COLLECTION).add(order).await()
            documentReference.id
        }

    override fun ordersByUser(email: String?): Flow<List<Order>> {
        return firestore.collection(ORDER_COLLECTION).whereEqualTo("userId", email)
            .dataObjects()
    }

    override fun orders(): Flow<List<Order>> {
        return firestore.collection(ORDER_COLLECTION).dataObjects()
    }

    override suspend fun getUserDetail(mail: String): GetUserState {
        return try {
            println("getUserDetail")
            val querySnapshot = firestore.collection(USER_COLLECTION)
                .whereEqualTo("email", mail)
                .get().await()
            val result = querySnapshot?.toObjects<User>()?.firstOrNull()
            if (result == null) {
                GetUserState.UserNotFound
            } else {
                GetUserState.OnUser(result)
            }
        } catch (e: Exception) {
            println("Failed to get user: ${e.message}")
            GetUserState.UserNotFound
        }
    }

    override suspend fun saveUser(user: User): CreateUserState {
        println("saveUser")
        return try {
            firestore.collection(USER_COLLECTION).add(user).await()
            CreateUserState.Created
        } catch (e: Exception) {
            println("Failed to save user: ${e.message}")
            CreateUserState.CreateError("Failed to save user: ${e.message}")
        }
    }

    override suspend fun checkUserIsAdmin(email: String): Boolean {
        return try {
            val documentSnapshot = firestore.collection(ADMIN_COLLECTION)
                .document("admin-mails")
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val emails = documentSnapshot["mail"] as? List<String>
                emails?.contains(email) ?: false
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PRODUCT_COLLECTION = "products"
        private const val USER_COLLECTION = "users"
        private const val ORDER_COLLECTION = "orders"
        private const val ADMIN_COLLECTION = "admin"

        private const val SAVE_PRODUCT_TRACE = "saveProduct"
        private const val CREATE_ORDER_TRACE = "createOrder"
        private const val UPDATE_PRODUCT_TRACE = "updateProduct"
    }
}