package com.example.organizeit.contentScreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizeit.ContentReceivedFromOutsideApp
import com.example.organizeit.ContentReceivedFromCamera
import com.example.organizeit.contentScreen.data.ContentDao
import com.example.organizeit.contentScreen.data.ContentModel
import com.example.organizeit.contentScreen.data.FolderContentModel
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ContentScreenViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contentDao:ContentDao
):ViewModel(){
    private val _folderList = MutableStateFlow<List<FolderContentModel>>(emptyList())
    val folderList = _folderList.asStateFlow()

    private var _selectedContentUri = MutableStateFlow<List<ContentModel>>(emptyList())
    val selectedContentUri = _selectedContentUri.asStateFlow()
    private var _parentFolder=MutableStateFlow("")
    private var parentFolder=_parentFolder.asStateFlow()

    private var _projectContentTypeViewOnScreen = MutableStateFlow(ProjectContentTypeString.Photos.value)
    val projectContentTypeViewOnScreen = _projectContentTypeViewOnScreen.asStateFlow()

    fun setProjectContentTypeViewOnScreen(contentType:String){
        _projectContentTypeViewOnScreen.value=contentType
    }

    private val _folderName= MutableStateFlow("")
    val folderName=_folderName.asStateFlow()

    private val _isAddNewFolderDialogVisible= MutableStateFlow(false)
    val isAddNewFolderDialogVisible=_isAddNewFolderDialogVisible.asStateFlow()

    fun setProjectName(name:String){
        _folderName.value=name
    }


    suspend fun addContent(id:String,uri:List<Uri>){
        uri.forEach {
            val contentResolver = context.contentResolver
            val inputStream = contentResolver.openInputStream(it)
            val file = File(context.filesDir, UUID.randomUUID().toString())
            val outputStream = FileOutputStream(file)
            inputStream?.use { input->
                outputStream.use{output->
                    input.copyTo(output)
                }
            }
            contentDao.insert(ContentModel(uri=file.absolutePath,type = projectContentTypeViewOnScreen.value, parentFolder = id))
        }
    }

    suspend fun fillInitialProjectPhotoData(id:String){
        contentDao.getContent(id).collect{
            _selectedContentUri.value=it
        }
    }


    fun enableAddNewProjectDialog(){
        _isAddNewFolderDialogVisible.value=true
    }
    fun disableAddNewProjectDialog(){
        _isAddNewFolderDialogVisible.value=false
        _folderName.value=""
    }
    suspend fun createFolder() {
        contentDao.insertFolderTable(FolderContentModel(folderName = folderName.value, parentId = parentFolder.value))
        disableAddNewProjectDialog()
    }
    fun setParentFolderId(id:String){
        _parentFolder.value=id
        viewModelScope.launch {
            contentDao.getFolderByParentId(id).collect{
                _folderList.value=it
            }
        }
    }

    suspend fun saveContent(context: Context){
        if(ContentReceivedFromOutsideApp.isCRFOAvailable.value){
            addContentReceivedFromOutsideAppInDb(context)
        }
        if(ContentReceivedFromCamera.isCRFCAvailable.value){
            if(ContentReceivedFromCamera.CameraPhotoUris.isNotEmpty()){addPhotoReceivedFromCamera()}
            if(ContentReceivedFromCamera.videUri!=""){addVideoReceivedFromCamera() }
        }
    }

    private suspend fun addVideoReceivedFromCamera() {
        contentDao.insert(ContentModel(uri=ContentReceivedFromCamera.videUri,type = ProjectContentTypeString.Videos.value, parentFolder = parentFolder.value))
        ContentReceivedFromCamera.clear()
    }

    private suspend fun addPhotoReceivedFromCamera() {
        val bitmaps=ContentReceivedFromCamera.CameraPhotoUris
        bitmaps.forEach { bitmap->
            val uri=convertBitMapToUri(bitmap)
            if(uri!==null){
                contentDao.insert(ContentModel(uri=uri,type = ProjectContentTypeString.Photos.value, parentFolder = parentFolder.value))
            }
        }
        ContentReceivedFromCamera.clear()
    }

    private  fun convertBitMapToUri(bitmap: Bitmap): String? {
        var outputStream: FileOutputStream? = null
        val file = File(context.filesDir, "Photos_From_Camera"+UUID.randomUUID().toString())
        try {
            outputStream = FileOutputStream(file)
            // Compress the bitmap to JPEG format with 100% quality
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            // Return the Uri of the saved file
            return file.absolutePath
        } catch (e: IOException) {
            Log.e("saveBitmapToFile", "Error saving bitmap to file: ${e.message}", e)
            return null
        } finally {
            // Ensure the stream is closed, even if an error occurs
            outputStream?.close()
        }
    }


    private suspend fun addContentReceivedFromOutsideAppInDb(context: Context){
        if(ContentReceivedFromOutsideApp.sharedFileUri!=null){
            val contentUri= ContentReceivedFromOutsideApp.sharedFileUri
            val mimeType:String?= context.contentResolver.getType(contentUri!!)
            val type:String = categorizeFile(mimeType)
            contentDao.insert(ContentModel(uri=contentUri.toString(),type = type, parentFolder = parentFolder.value))
        }
        if(ContentReceivedFromOutsideApp.sharedFileUris.isNotEmpty()){
            ContentReceivedFromOutsideApp.sharedFileUris.forEach {
                val mimeType:String?= context.contentResolver.getType(it)
                val type:String = categorizeFile(mimeType)
                contentDao.insert(ContentModel(uri=it.toString(),type = type, parentFolder = parentFolder.value))
            }
        }
        ContentReceivedFromOutsideApp.clear()
    }

    private fun categorizeFile(mimeType: String?): String {
        return when {
            mimeType == null -> "Unknown"
            mimeType.startsWith("image/") -> ProjectContentTypeString.Photos.value
            mimeType.startsWith("video/") -> ProjectContentTypeString.Videos.value
            mimeType.startsWith("audio/") -> ProjectContentTypeString.Audios.value
            mimeType.startsWith("application/") -> {
                when (mimeType) {
                    "application/pdf" ,
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ,
                    "application/vnd.ms-excel",
                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" ,
                    "application/vnd.ms-powerpoint",
                    "application/vnd.openxmlformats-officedocument.presentationml.presentation" -> ProjectContentTypeString.Documents.value
                    else -> "Unknown"
                }
            }
            else -> "Unknown"
        }
    }}
