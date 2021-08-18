package br.com.douglasmotta.whitelabeltutorial.domain.usercase

import android.net.Uri
import br.com.douglasmotta.whitelabeltutorial.domain.model.Product

interface CreateProductUseCase {
    suspend operator fun invoke(description: String, prince:Double,image: Uri) :Product
}