package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BoostHistoryDao
import com.example.data.dao.GameDao
import com.example.data.dao.GfxPresetDao
import com.example.data.entity.BoostHistoryEntity
import com.example.data.entity.GameEntity
import com.example.data.entity.GfxPresetEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [GameEntity::class, BoostHistoryEntity::class, GfxPresetEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
    abstract fun boostHistoryDao(): BoostHistoryDao
    abstract fun gfxPresetDao(): GfxPresetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "game_booster_vip_db"
                ).addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialPresets(database.gfxPresetDao())
                    }
                }
            }

            private suspend fun populateInitialPresets(presetDao: GfxPresetDao) {
                val defaultPresets = listOf(
                    GfxPresetEntity(
                        profileName = "Ultra 144Hz Competitive",
                        resolution = "1080p FHD",
                        fps = 144,
                        graphics = "Smooth",
                        msaa = "Off",
                        shadows = "Low",
                        gpuTurbo = true,
                        vulkanOptimization = true
                    ),
                    GfxPresetEntity(
                        profileName = "Extreme HDR Cinematic",
                        resolution = "2K Ultra HD",
                        fps = 90,
                        graphics = "HDR Ultra",
                        msaa = "4x MSAA",
                        shadows = "Ultra",
                        gpuTurbo = true,
                        vulkanOptimization = true
                    ),
                    GfxPresetEntity(
                        profileName = "Balanced eSports Stable",
                        resolution = "1080p FHD",
                        fps = 120,
                        graphics = "Balanced",
                        msaa = "2x MSAA",
                        shadows = "Medium",
                        gpuTurbo = true,
                        vulkanOptimization = false
                    ),
                    GfxPresetEntity(
                        profileName = "Battery Saver Long Run",
                        resolution = "720p HD",
                        fps = 60,
                        graphics = "Smooth",
                        msaa = "Off",
                        shadows = "Off",
                        gpuTurbo = false,
                        vulkanOptimization = true
                    )
                )
                presetDao.insertPresets(defaultPresets)
            }
        }
    }
}
