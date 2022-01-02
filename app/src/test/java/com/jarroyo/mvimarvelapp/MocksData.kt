package com.jarroyo.mvimarvelapp

import com.jarroyo.mvimarvelapp.data.local.model.CharacterEntity
import com.jarroyo.mvimarvelapp.domain.model.UiModel

val mockCharacterUIModel =
    UiModel(id = 1, name = "name", description = "Description", imageHomeUrl = "image")
val mockCharacterUIModelList = listOf(mockCharacterUIModel)
val mockException = Exception("MockException")

val mockCharacterEntity =
    CharacterEntity(id = 1, name = "name", description = "Description", image = "image")
val mockCharacterEntityList = listOf(mockCharacterEntity)