package com.axiom.di

import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.axiom.BuildConfig
import com.axiom.data.database.AxiomDatabase
import com.axiom.data.database.MIGRATION_1_2
import com.axiom.data.database.MIGRATION_2_3
import com.axiom.data.database.MIGRATION_3_4
import com.axiom.data.database.TrashDao
import com.axiom.data.preferences.SettingsDataStore
import com.axiom.data.preferences.dataStore
import com.axiom.data.repository.TrashRepository
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import java.io.File

@Module
@ComponentScan("com.axiom")
object AppModule

@Singleton
fun provideContentResolver(context: Context): ContentResolver = context.contentResolver

@Singleton
fun provideAppDatabase(context: Context) = Room
    .databaseBuilder(
        context = context.applicationContext,
        klass = AxiomDatabase::class.java,
        name = "axiom_database"
    )
    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
    .apply {
        if (BuildConfig.DEBUG) {
            fallbackToDestructiveMigration(dropAllTables = true)
        }
    }
    .build()

@Singleton
fun provideRecentFileDao(db: AxiomDatabase) = db.recentFileDao()

@Singleton
fun provideRecentProjectDao(db: AxiomDatabase) = db.recentProjectDao()

@Singleton
fun providePluginTabDao(db: AxiomDatabase) = db.pluginTabDao()

@Singleton
fun provideTrashDao(db: AxiomDatabase) = db.trashDao()

@Singleton
fun provideTrashRepository(context: Context, trashDao: TrashDao): TrashRepository =
    TrashRepository(trashDao, File(context.filesDir, "trash/items"))

@Singleton
fun provideAppPreferences(context: Context): SettingsDataStore = context.dataStore
