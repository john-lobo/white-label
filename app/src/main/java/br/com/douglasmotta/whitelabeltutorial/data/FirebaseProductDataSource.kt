package br.com.douglasmotta.whitelabeltutorial.data

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.BuildConfig.FIREBASE_FLAVOR_COLLECTION
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import br.com.douglasmotta.whitelabeltutorial.util.COLLECTION_PRODUCTS
import br.com.douglasmotta.whitelabeltutorial.util.COLLECTION_ROOT
import br.com.douglasmotta.whitelabeltutorial.util.STORAGE_IMAGES
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class FirebaseProductDataSource @Inject constructor(
    firebaseFirestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage
): ProductDataSource {

    //data/car/products/timestamp/productA
    //data/bike/products/timestamp/productB

    private val documentReferente = firebaseFirestore
        .document("$COLLECTION_ROOT/$FIREBASE_FLAVOR_COLLECTION")

    private val storageReference  =firebaseStorage.reference

    override suspend fun getProducts(): List<Product> {
        return suspendCoroutine { continuation ->
            val productReference = documentReferente.collection(COLLECTION_PRODUCTS)
            productReference.get().addOnSuccessListener { documents ->
                val products = mutableListOf<Product>()
                for (document in documents){
                    document.toObject(Product::class.java).run {
                        products.add(this)
                    }
                }
                continuation.resumeWith(Result.success(products))
            }
            productReference.get().addOnFailureListener {exception ->
                continuation.resumeWith(Result.failure(exception))
            }
        }

    }

    override suspend fun uploadProductImage(imageUri: Uri): String {
        return suspendCoroutine { continuation ->
            val randomKey = UUID.randomUUID()
            val childReference = storageReference.child(
                "$STORAGE_IMAGES/$FIREBASE_FLAVOR_COLLECTION/$randomKey"
            )

            childReference.putFile(imageUri)
                .addOnSuccessListener {taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                        val path = uri.toString()
                        continuation.resumeWith(Result.success(path))
                    }
                }
                .addOnFailureListener{ exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
            }
    }

    override suspend fun createProduct(product: Product): Product {
        return suspendCoroutine {continuation ->
            documentReferente
                .collection(COLLECTION_PRODUCTS)
                .document(System.currentTimeMillis().toString())
                .set(product)
                .addOnSuccessListener {
                    continuation.resumeWith(Result.success(product))
                }
                .addOnFailureListener {exception ->
                    continuation.resumeWith(Result.failure(exception))
                }
        }
    }
}