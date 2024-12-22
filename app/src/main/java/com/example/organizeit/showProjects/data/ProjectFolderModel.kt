package com.example.organizeit.showProjects.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


@Entity(tableName = "ProjectFolderTable")
data class ProjectFolderModel(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val projectName: String
)

