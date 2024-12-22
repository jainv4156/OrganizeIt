package com.example.organizeit.contentScreen.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "FolderTable")
data class FolderContentModel (
    @PrimaryKey
    val id:String=UUID.randomUUID().toString(),
    val folderName:String="",
    val parentId:String=""
)