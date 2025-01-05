package com.example.organizeit.showProjects.ui

import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.organizeit.contentScreen.uiComponents.ScreenB
import com.example.organizeit.showProjects.ShowProjectViewmodel
import com.example.organizeit.ui.theme.ForlderColor


@Composable
fun ProjectListView(navController:NavHostController) {
    val viewModel: ShowProjectViewmodel = hiltViewModel()
    val projectFolderList=viewModel.projectFolderList.collectAsState()
    val context = LocalContext.current



    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {isGranted: Boolean ->

    }
    fun requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            launcher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
        }else{
            launcher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
    fun checkPermission():Boolean {
        if(context.checkSelfPermission(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) android.Manifest.permission.READ_MEDIA_IMAGES else
                    android.Manifest.permission.READ_EXTERNAL_STORAGE

        )==android.content.pm.PackageManager.PERMISSION_GRANTED){
            return true
        }
        requestPermission()
        return false
    }
    LazyVerticalGrid (
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.padding(16.dp,36.dp)){
        items(projectFolderList.value){ item->
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .clickable {
                            if(checkPermission()){
                                navController.navigate(ScreenB(item.id))
                            }
                        }
                ){
                    Icon(imageVector = Icons.Default.Folder,
                        contentDescription = "FolderIcon",
                        tint = ForlderColor,
                        modifier = Modifier
                            .padding(8.dp, 2.dp)
                            .size(150.dp))
                    Text(text = item.projectName, fontWeight = FontWeight.Bold , fontSize = 20.sp )
                }
            }
    }

}


