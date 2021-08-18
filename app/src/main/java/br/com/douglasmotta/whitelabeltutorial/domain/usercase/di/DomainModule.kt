package br.com.douglasmotta.whitelabeltutorial.domain.usercase.di

import br.com.douglasmotta.whitelabeltutorial.domain.usercase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface DomainModule {

    @Binds
    fun bindCreateProductUseCase(useCase: CreateProductUserImpl): CreateProductUseCase

    @Binds
    fun bindUpdateProductUseCase(useCase: UploadProductImageUseCaseImpl): UploadProductImageUseCase

    @Binds
    fun bindGetProductUseCase(useCase: GetProductCaseImpl): GetProductUseCase
}