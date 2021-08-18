package br.com.douglasmotta.whitelabeltutorial.domain.usercase

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.data.ProductRepository
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import kotlinx.coroutines.Dispatchers
import java.util.*
import javax.inject.Inject

class CreateProductUserImpl @Inject constructor(
    private val repository: ProductRepository,
    private val uploadProductImageUseCase: UploadProductImageUseCase
):CreateProductUseCase {
    override suspend fun invoke(description: String, prince: Double, image: Uri): Product {
       return try {
            val imageUrl = uploadProductImageUseCase(image)
            val product = Product(
                id = UUID.randomUUID().toString(),
                description = description,
                price = prince,
                imageUrl = imageUrl)

            repository.createProduct(product)

       } catch (e: Exception){
               throw e
           }
    }
}