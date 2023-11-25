package com.autospare.service

import androidx.core.net.toUri
import androidx.core.os.trace
import com.autospare.data.Product
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
class StorageServiceImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val auth: AccountService,
) :
    StorageService {

    override val products: Flow<List<Product>>
        get() = firestore.collection(PRODUCT_COLLECTION)
            .dataObjects()

    override suspend fun getProduct(productId: String): Product? =
        firestore.collection(PRODUCT_COLLECTION).document(productId).get().await().toObject()

    suspend fun uploadFileToStorageAndGetUrl(file: File): String = withContext(Dispatchers.IO) {
        val storageReference = firebaseStorage.getReference("/products")
        val ref = storageReference.child("/${file.name}")
        val uploadTask = ref.putFile(file.toUri())
        ref.downloadUrl.result.path.toString()
    }

    suspend fun saveProductToFirestore(product: Product): String = withContext(Dispatchers.IO) {
        firestore.collection(PRODUCT_COLLECTION).add(product).await().id
    }

//    override suspend fun save(product: Product) {
//        val imageUrl = uploadFileToStorageAndGetUrl(File(product.imageUrl))
//
//        // Update the product with the image URL
//        val updatedProduct = product.copy(imageUrl = imageUrl)
//
//        Log.i("updatedProduct", "updatedProduct $updatedProduct")
//
//        // Save the updated product to Firestore
//        val productId = saveProductToFirestore(updatedProduct)
//
//        // Use the productId or perform further actions if needed
//    }

    override suspend fun save(product: Product): String = withContext(Dispatchers.IO) {
        trace(SAVE_PRODUCT_TRACE) {
            val storageReference = firebaseStorage.getReference("/products")
            val file = File(product.imageUrl)
            val ref = storageReference.child("/${LocalTime.now().toSecondOfDay()}.jpg")

            // Upload the file to storage
            val uploadTask = ref.putFile(file.toUri())
            await(uploadTask) // Wait for the upload task to complete

            // Get the download URL after the upload is complete
            val storageUrl = ref.downloadUrl.await().toString()

            // Update the product with the image URL
            val updatedProduct = product.copy(imageUrl = storageUrl)

            // Save the updated product to Firestore and return the document ID
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

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val PRODUCT_COLLECTION = "products"
        private const val SAVE_PRODUCT_TRACE = "saveProduct"
        private const val UPDATE_PRODUCT_TRACE = "updateProduct"
    }
}