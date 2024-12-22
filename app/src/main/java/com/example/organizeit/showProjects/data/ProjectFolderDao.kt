package com.example.organizeit.showProjects.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectFolderDao {

    @Upsert
    suspend fun insertProjectFolder(projectFolderModel: ProjectFolderModel)

    @Query("SELECT * FROM ProjectFolderTable ")
    fun getAllProjectFolders(): Flow<List<ProjectFolderModel>>

    @Delete
    suspend fun deleteProjectFolder(projectFolderModel: ProjectFolderModel)

}