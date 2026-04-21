package com.afquintana.calculator.di

import com.afquintana.calculator.domain.usecase.EvaluateExpressionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideEvaluateExpressionUseCase(): EvaluateExpressionUseCase = EvaluateExpressionUseCase()
}
