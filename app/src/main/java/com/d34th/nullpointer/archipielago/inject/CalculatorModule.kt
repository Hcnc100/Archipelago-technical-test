package com.d34th.nullpointer.archipielago.inject

import android.content.Context
import com.d34th.nullpointer.archipielago.domain.CalculatorRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CalculatorModule {

    @Provides
    @Singleton
    fun provideCalculator(
        @ApplicationContext context: Context
    ): CalculatorRepoImpl = CalculatorRepoImpl(context)
}