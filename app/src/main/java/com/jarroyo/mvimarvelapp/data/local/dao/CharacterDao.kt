package com.jarroyo.mvimarvelapp.data.local.dao

import androidx.room.*
import com.jarroyo.mvimarvelapp.data.local.model.CharacterEntity
import com.jarroyo.mvimarvelapp.data.local.model.TABLE_CHARACTER

@Dao
interface CharacterDao {
    @Query("SELECT * FROM $TABLE_CHARACTER ORDER BY name ASC")
    fun getAll(): List<CharacterEntity>

    @Query("SELECT * FROM $TABLE_CHARACTER WHERE id=:id")
    fun getCharacter(id: Long): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(list: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: CharacterEntity)

    @Delete
    fun delete(breedEntity: CharacterEntity)

    @Query("DELETE FROM $TABLE_CHARACTER WHERE id=:id")
    fun delete(id: Long)

    @Query("DELETE FROM $TABLE_CHARACTER")
    fun deleteAll()
}