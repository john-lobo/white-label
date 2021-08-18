package br.com.douglasmotta.whitelabeltutorial.domain.usercase

import br.com.douglasmotta.whitelabeltutorial.domain.model.Product

interface GetProductUseCase {

    suspend operator fun invoke(): List<Product>
}