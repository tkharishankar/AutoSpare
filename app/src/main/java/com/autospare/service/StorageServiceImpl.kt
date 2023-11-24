package com.autospare.service

import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.core.os.trace
import com.autospare.data.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject

/**
 * Author: Hari K
 * Date: 23/11/2023.
 */
class StorageServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val auth: AccountService,
) :
    StorageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val products: Flow<List<Product>>
        get() =
            auth.currentUser.flatMapLatest { user ->
                firestore.collection(PRODUCT_COLLECTION).whereEqualTo(USER_ID_FIELD, user.email)
                    .dataObjects()
            }

    override suspend fun getProduct(productId: String): Product? =
        firestore.collection(PRODUCT_COLLECTION).document(productId).get().await().toObject()

    override suspend fun save(product: Product): String =
        trace(SAVE_PRODUCT_TRACE) {
            val storageReference = firebaseStorage.getReference("/products")
            val file = File(product.imageUrl)
            val ref = storageReference.child("/${file.name}")
            val uploadTask = ref.putFile(file.toUri())
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener {
                // Handle unsuccessful uploads
            }.addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.result.path
                Log.i("Uploaded url", "storage url" + ref.downloadUrl.result.path.toString())
            }
            firestore.collection(PRODUCT_COLLECTION).add(product).await().id
        }

    override suspend fun update(product: Product): Unit =
        trace(UPDATE_PRODUCT_TRACE) {
            firestore.collection(PRODUCT_COLLECTION).document(product.name).set(product).await()
        }

    override suspend fun delete(productId: String) {
        firestore.collection(PRODUCT_COLLECTION).document(productId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PRODUCT_COLLECTION = "products"
        private const val SAVE_PRODUCT_TRACE = "saveProduct"
        private const val UPDATE_PRODUCT_TRACE = "updateProduct"
    }
}