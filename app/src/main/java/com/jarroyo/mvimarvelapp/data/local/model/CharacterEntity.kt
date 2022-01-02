package com.jarroyo.mvimarvelapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jarroyo.mvimarvelapp.domain.model.UiModel

const val TABLE_CHARACTER = "TABLE_CHARACTER"

@Entity(tableName = TABLE_CHARACTER)
class CharacterEntity(
    @PrimaryKey
    var id: Long,
    var name: String? = "",
    var description: String? = "",
    var image: String? = ""
)

fun CharacterEntity.toModel() = UiModel(id, name, description, image)

fun List<CharacterEntity>.toModel() =
    this.map {
        it.toModel()
    }
