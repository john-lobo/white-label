package br.com.douglasmotta.whitelabeltutorial.domain.usercase

import android.net.Uri

interface UploadProductImageUseCase {

    suspend operator fun invoke(imageUri: Uri): String
}