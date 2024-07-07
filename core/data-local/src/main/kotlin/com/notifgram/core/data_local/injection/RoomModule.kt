package com.notifgram.core.data_local.injection

import android.app.Application
import androidx.room.Room
import com.notifgram.core.common.Cryptography
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.data_local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SupportFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RoomModule {

    private const val DATABASE_NAME: String = "app_db"

    @Provides
    @Singleton
    fun provideCryptography() = Cryptography()

//    @Provides
//    @Singleton
//    fun providePassphraseManger(
//        appSettingsDataSourceImpl: AppSettingsDataSourceImpl,
//        cryptography: Cryptography,
//        @Named("ForPassphraseManager") keystoreManager: KeystoreManager
//    ): PassphraseManager = PassphraseManager(
//        appSettingsDataSourceImpl,
//        cryptography,
//        keystoreManager
//    )

    @Provides
    @Singleton
    fun provideSupportFactory(
        passphraseManager: PassphraseManager
    ): SupportFactory {

        val passphrase = runBlocking { passphraseManager.start() }
        i("passphrase=$passphrase")
        return SupportFactory(passphrase)

    }

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application, supportFactory: SupportFactory): AppDatabase =
        Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            DATABASE_NAME  //application.getString(R.string.database) me: maybe it would be better for security reason not to use strings.
        ).apply {
            openHelperFactory(supportFactory) // Used for Room encryption using sqlcipher

            //if (BuildConfig.DEBUG)
            //Allows Room to destructively recreate database tables if Migrations that would
            // migrate old database schemas to the latest schema version are not found.
            //Source:https://developer.android.com/reference/android/arch/persistence/room/RoomDatabase.Builder
            fallbackToDestructiveMigration()

        }.build()

}
