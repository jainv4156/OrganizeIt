package com.example.organizeit.contentScreen.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "ContentTable")
data class ContentModel(
    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    val uri: String="",
    val type:String="",
    val parentFolder:String=""
)
