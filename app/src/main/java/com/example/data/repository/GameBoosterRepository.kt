package com.example.data.repository

import com.example.data.dao.BoostHistoryDao
import com.example.data.dao.GameDao
import com.example.data.dao.GfxPresetDao
import com.example.data.entity.BoostHistoryEntity
import com.example.data.entity.GameEntity
import com.example.data.entity.GfxPresetEntity
import kotlinx.coroutines.flow.Flow

class GameBoosterRepository(
    private val gameDao: GameDao,
    private val boostHistoryDao: BoostHistoryDao,
    private val gfxPresetDao: GfxPresetDao
) {
    val allGames: Flow<List<GameEntity>> = gameDao.getAllGames()
    val recentHistory: Flow<List<BoostHistoryEntity>> = boostHistoryDao.getRecentHistory()
    val allPresets: Flow<List<GfxPresetEntity>> = gfxPresetDao.getAllPresets()

    suspend fun insertGame(game: GameEntity): Long = gameDao.insertGame(game)
    suspend fun updateGame(game: GameEntity) = gameDao.updateGame(game)
    suspend fun deleteGame(game: GameEntity) = gameDao.deleteGame(game)
    suspend fun incrementLaunch(gameId: Long) = gameDao.incrementLaunch(gameId)
    suspend fun toggleFavorite(gameId: Long) = gameDao.toggleFavorite(gameId)
    suspend fun getGameByPackage(packageName: String): GameEntity? = gameDao.getGameByPackage(packageName)

    suspend fun recordBoostHistory(history: BoostHistoryEntity) = boostHistoryDao.insertHistory(history)
    suspend fun clearHistory() = boostHistoryDao.clearHistory()

    suspend fun savePreset(preset: GfxPresetEntity) = gfxPresetDao.insertPreset(preset)
}
