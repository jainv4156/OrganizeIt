package com.example.organizeit.showProjects

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.organizeit.showProjects.data.ProjectFolderDao
import com.example.organizeit.showProjects.data.ProjectFolderModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShowProjectViewmodel @Inject constructor( private val dao:ProjectFolderDao) :ViewModel(){


    private val _projectFolderList = MutableStateFlow<List<ProjectFolderModel>>(emptyList())
    val projectFolderList = _projectFolderList.asStateFlow()

    private val _projectName= MutableStateFlow("")
    val projectName=_projectName.asStateFlow()

    private val _isAddNewProjectDialogVisible= MutableStateFlow(false)
    val isAddNewProjectDialogVisible=_isAddNewProjectDialogVisible.asStateFlow()

    fun setProjectName(name:String){
        _projectName.value=name
    }

    fun enableAddNewProjectDialog(){
        _isAddNewProjectDialogVisible.value=true
    }
    fun disableAddNewProjectDialog(){
        _isAddNewProjectDialogVisible.value=false
        _projectName.value=""
    }   


    init {
        viewModelScope.launch {
            dao.getAllProjectFolders().collect {list->
                _projectFolderList.value = list
            }
        }
    }

    suspend fun createProjectFolder() {
        dao.insertProjectFolder(ProjectFolderModel(projectName = projectName.value))
        disableAddNewProjectDialog()
    }


}