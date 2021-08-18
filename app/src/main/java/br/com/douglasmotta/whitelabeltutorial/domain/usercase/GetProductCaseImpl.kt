package br.com.douglasmotta.whitelabeltutorial.domain.usercase

import br.com.douglasmotta.whitelabeltutorial.data.ProductRepository
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product
import javax.inject.Inject

class GetProductCaseImpl @Inject constructor(
    private val repository: ProductRepository
): GetProductUseCase {
    override suspend fun invoke(): List<Product> {
        return repository.getProducts()
    }
}