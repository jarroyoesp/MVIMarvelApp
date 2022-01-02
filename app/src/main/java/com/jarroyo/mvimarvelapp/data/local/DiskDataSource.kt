package com.jarroyo.mvimarvelapp.data.local

import com.jarroyo.mvimarvelapp.data.local.model.CharacterEntity
import com.jarroyo.mvimarvelapp.domain.model.UiModel
import com.jarroyo.mvimarvelapp.domain.model.toDomain

interface DiskDataSource {
    fun insertCharacter(characterEntity: CharacterEntity): Unit?
    fun removeCharacter(id: Long): Unit?
    fun getCharacterList(): Result<List<UiModel>?>
    fun getCharacter(id: Long): Result<UiModel?>

    fun deleteAllTables()
}

class DiskDataSourceImpl(private val database: AppDatabase): DiskDataSource {

    override fun insertCharacter(characterEntity: CharacterEntity) = database.characterDao().insert(characterEntity)
    override fun removeCharacter(id: Long) = database.characterDao().delete(id)
    override fun getCharacterList(): Result<List<UiModel>?>{
        return Result.success(database.characterDao().getAll().toDomain())
    }
    override fun getCharacter(id: Long): Result<UiModel?> {
        return database.characterDao().getCharacter(id)?.let {
            Result.success(it.toDomain())
        } ?: Result.failure(Exception("Character not found"))

    }

    override fun deleteAllTables() {
        database.characterDao().deleteAll()
    }
}