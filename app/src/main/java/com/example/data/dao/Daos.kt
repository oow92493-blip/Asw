package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.BoostHistoryEntity
import com.example.data.entity.GameEntity
import com.example.data.entity.GfxPresetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM games ORDER BY isFavorite DESC, lastPlayedTimestamp DESC, id DESC")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id LIMIT 1")
    suspend fun getGameById(id: Long): GameEntity?

    @Query("SELECT * FROM games WHERE packageName = :packageName LIMIT 1")
    suspend fun getGameByPackage(packageName: String): GameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGames(games: List<GameEntity>)

    @Update
    suspend fun updateGame(game: GameEntity)

    @Delete
    suspend fun deleteGame(game: GameEntity)

    @Query("UPDATE games SET launchCount = launchCount + 1, lastPlayedTimestamp = :timestamp WHERE id = :id")
    suspend fun incrementLaunch(id: Long, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE games SET isFavorite = NOT isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long)
}

@Dao
interface BoostHistoryDao {
    @Query("SELECT * FROM boost_history ORDER BY timestamp DESC LIMIT 30")
    fun getRecentHistory(): Flow<List<BoostHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: BoostHistoryEntity)

    @Query("DELETE FROM boost_history")
    suspend fun clearHistory()
}

@Dao
interface GfxPresetDao {
    @Query("SELECT * FROM gfx_presets")
    fun getAllPresets(): Flow<List<GfxPresetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPreset(preset: GfxPresetEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresets(presets: List<GfxPresetEntity>)
}
