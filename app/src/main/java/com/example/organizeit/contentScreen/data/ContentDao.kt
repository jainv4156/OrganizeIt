package com.example.organizeit.contentScreen.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ContentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contentModel: ContentModel)

    @Delete
    suspend fun delete(contentModel: ContentModel)

    @Query("SELECT * FROM ContentTable WHERE parentFolder=:project")
    fun getContent(project:String): Flow<List<ContentModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolderTable(folderContentModel:FolderContentModel)

    @Query("SELECT * FROM FolderTable WHERE parentId=:id")
    fun getFolderByParentId(id:String): Flow<List<FolderContentModel>>

}