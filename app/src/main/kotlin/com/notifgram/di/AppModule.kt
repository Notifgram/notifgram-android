package com.notifgram.di

import com.notifgram.core.common.Cryptography
import com.notifgram.core.common.FileEncryptionManager
import com.notifgram.core.common.KeystoreManager
import com.notifgram.core.domain.usecase.UseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideUseCaseConfiguration() = UseCase.Configuration(Dispatchers.IO)

    @Singleton
    @Provides
    fun provideFileEncryptionManager(
        cryptography: Cryptography,
        @Named("ForFileEncryptionManager") keystoreManager: KeystoreManager
    ) = FileEncryptionManager(
        keystoreManager,
        cryptography,
    )

}