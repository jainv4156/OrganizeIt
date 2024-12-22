package com.example.organizeit.contentScreen

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizeit.ContentReceivedFromOutsideApp
import com.example.organizeit.contentScreen.data.ContentDao
import com.example.organizeit.contentScreen.data.ContentModel
import com.example.organizeit.contentScreen.data.FolderContentModel
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentScreenViewModel @Inject constructor(private val contentDao:ContentDao):ViewModel(){
    private val _folderList = MutableStateFlow<List<FolderContentModel>>(emptyList())
    val folderList = _folderList.asStateFlow()

    private var _selectedContentUri = MutableStateFlow<List<ContentModel>>(emptyList())
    val selectedContentUri = _selectedContentUri.asStateFlow()
    private var _parentFolder=MutableStateFlow("")
    var parentFolder=_parentFolder.asStateFlow()
//
//    private var _audioUri = MutableStateFlow<List<Uri>>(emptyList())
//    val audioUri = _audioUri.asStateFlow()
//
//    private var _documentUri = MutableStateFlow<List<Uri>>(emptyList())
//    val documentUri = _documentUri.asStateFlow()
//
//    private var _videoUri = MutableStateFlow<List<Uri>>(emptyList())
//    val videoUri = _videoUri.asStateFlow()

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

//    fun updateSelectedImagesUri(uri:List<Uri>){
//        _selectedContentUri.value+=uri
//
//    }
//    fun updateAudioUri(uri:List<Uri>){
//        _audioUri.value+=uri
//    }
//
//    fun updateDocumentUri(uri:List<Uri>){
//        _documentUri.value+=uri
//
//    }

    suspend fun addContent(id:String,uri:List<Uri>){
        uri.forEach {
            contentDao.insert(ContentModel(uri=it.toString(),type = projectContentTypeViewOnScreen.value, parentFolder = id))
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



    suspend fun addContentReceivedFromOutsideAppInDb(context: Context){
        Log.d("chulbulla",ContentReceivedFromOutsideApp.isCRFOAvailable.toString())
        Log.d("mulmula",ContentReceivedFromOutsideApp.sharedFileUris.toString())
        if(ContentReceivedFromOutsideApp.sharedFileUri!=null){
            val contentUri= ContentReceivedFromOutsideApp.sharedFileUri
            val mimeType:String?= context.contentResolver.getType(contentUri!!)
            val type:String = categorizeFile(mimeType)
            contentDao.insert(ContentModel(uri=contentUri.toString(),type = type, parentFolder = parentFolder.value))
            Log.d("kulla",contentUri.toString())
        }
        if(ContentReceivedFromOutsideApp.sharedFileUris.isNotEmpty()){
            ContentReceivedFromOutsideApp.sharedFileUris.forEach {
                val mimeType:String?= context.contentResolver.getType(it)
                val type:String = categorizeFile(mimeType)
                contentDao.insert(ContentModel(uri=it.toString(),type = type, parentFolder = parentFolder.value))
            }
            Log.d("rulla",ContentReceivedFromOutsideApp.sharedFileUris.toString())
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
