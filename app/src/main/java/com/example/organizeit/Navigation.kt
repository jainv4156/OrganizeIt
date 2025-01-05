package com.example.organizeit

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.organizeit.CameraScreen.ui.CameraScreenView
import com.example.organizeit.CameraScreen.ui.ScreenC
import com.example.organizeit.contentScreen.uiComponents.ProjectScreenView
import com.example.organizeit.contentScreen.uiComponents.ScreenB
import com.example.organizeit.showProjects.ui.ShowProjectView
import com.example.organizeit.showProjects.ui.ScreenA

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController()
){
    NavHost(navController = navController, startDestination = ScreenA) {

        composable<ScreenA>{
            ShowProjectView(navController)
        }
        composable<ScreenB>{
            val args=it.toRoute<ScreenB>()
            ProjectScreenView(args.parentFolderId,navController)
        }
        composable<ScreenC>{
            CameraScreenView(navController)
        }
    }
}