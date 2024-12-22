package com.example.organizeit.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.organizeit.contentScreen.data.ContentDao
import com.example.organizeit.contentScreen.data.ContentModel
import com.example.organizeit.contentScreen.data.FolderContentModel
import com.example.organizeit.showProjects.data.ProjectFolderDao
import com.example.organizeit.showProjects.data.ProjectFolderModel

@Database(
    entities = [ContentModel::class,ProjectFolderModel::class,FolderContentModel::class],
    version = 1
)

abstract class Database:RoomDatabase() {
    abstract val contentDao:ContentDao
    abstract val projectFolderDao: ProjectFolderDao
    companion object {
        fun getDatabase(context: Context): com.example.organizeit.data.Database {
            return instance1 ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.example.organizeit.data.Database::class.java,
                    "app_database"
                ).build()
                instance1 = instance
                instance
            }
        }
        private var instance1: com.example.organizeit.data.Database? = null
    }
}
