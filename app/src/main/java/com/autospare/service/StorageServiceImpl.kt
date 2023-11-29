package com.autospare.service

import androidx.core.net.toUri
import androidx.core.os.trace
import com.autospare.data.Order
import com.autospare.data.Product
import com.autospare.data.Status
import com.google.android.gms.tasks.Tasks.await
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalTime
import javax.inject.Inject

/**
 * Author: Senthil
 * Date: 23/11/2023.
 */
class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val auth: AccountService,
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
            val ref = storageReference.child("/${LocalTime.now().toSecondOfDay()}.jpg")
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

    override suspend fun createOrder(products: List<Product>): String =
        trace(CREATE_ORDER_TRACE) {
            val order = Order(
                orderId = LocalTime.now().toSecondOfDay().toString(),
                products = products.filter { it.isSelected }.map { it.productId },
                createdTimestamp = LocalTime.now().toNanoOfDay(),
                status = Status.CREATED
            )
            val documentReference =
                firestore.collection(PRODUCT_ORDER_COLLECTION).add(order).await()
            documentReference.id
        }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PRODUCT_COLLECTION = "products"
        private const val PRODUCT_ORDER_COLLECTION = "orders"

        private const val SAVE_PRODUCT_TRACE = "saveProduct"
        private const val CREATE_ORDER_TRACE = "createOrder"
        private const val UPDATE_PRODUCT_TRACE = "updateProduct"
    }
}