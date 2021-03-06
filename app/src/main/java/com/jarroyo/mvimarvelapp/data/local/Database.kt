package com.jarroyo.mvimarvelapp.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jarroyo.mvimarvelapp.data.local.dao.CharacterDao
import com.jarroyo.mvimarvelapp.data.local.model.CharacterEntity

@Database(
    entities = [CharacterEntity::class], version = 1
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    companion object {
        private const val DATABASE_NAME: String = "marvel_db"

        fun createInstance(appContext: Application):
                AppRoomDatabase = Room.databaseBuilder(
            appContext,
            AppRoomDatabase::class.java, DATABASE_NAME
        ).build()
    }
}
