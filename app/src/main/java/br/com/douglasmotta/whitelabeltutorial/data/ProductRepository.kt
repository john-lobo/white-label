package br.com.douglasmotta.whitelabeltutorial.data

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val dataSource: ProductDataSource
    ) {

    suspend fun getProducts(): List<Product>{
        return  dataSource.getProducts();
    }

    suspend fun uploadProductImage(imageUri: Uri): String{
        return dataSource.uploadProductImage(imageUri)
    }

    suspend fun createProduct(product: Product): Product{
        return dataSource.createProduct(product)

    }
}