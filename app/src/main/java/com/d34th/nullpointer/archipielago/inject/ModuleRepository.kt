package com.d34th.nullpointer.archipielago.inject

import com.d34th.nullpointer.archipielago.domain.CalculatorRepoImpl
import com.d34th.nullpointer.archipielago.domain.CalculatorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ModuleRepository {

    @Binds
    @Singleton
    abstract fun provideCalculator(
        calculatorRepoImpl: CalculatorRepoImpl
    ): CalculatorRepository
}