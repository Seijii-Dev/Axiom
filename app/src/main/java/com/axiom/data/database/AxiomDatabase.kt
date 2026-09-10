package com.axiom.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        RecentProjectEntity::class,
        RecentFileEntity::class,
        PluginTabEntity::class,
        TrashEntity::class
    ],
    version = 4,
    exportSchema = true
)
abstract class AxiomDatabase : RoomDatabase() {

    abstract fun recentProjectDao(): RecentProjectDao

    abstract fun recentFileDao(): RecentFileDao

    abstract fun pluginTabDao(): PluginTabDao

    abstract fun trashDao(): TrashDao
}
