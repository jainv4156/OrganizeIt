package com.example.organizeit.CameraScreen.ui

import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import kotlinx.serialization.Serializable
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.organizeit.CameraScreen.CameraScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun CameraScreenView (navHostController: NavHostController){

    val viewModel:CameraScreenViewModel= hiltViewModel()
    val context= LocalContext.current
    val cameraPermissions= arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.RECORD_AUDIO,
    )
    fun hasRequiredPermission():Boolean{
        return cameraPermissions.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            )== PermissionChecker.PERMISSION_GRANTED
    }}
    if(!hasRequiredPermission()){
        ActivityCompat.requestPermissions(
            context as Activity,cameraPermissions,0
        )
    }
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                CameraController.VIDEO_CAPTURE
            )
        }
    }
    fun takePhoto(controller: LifecycleCameraController, onPhotoTaken: (Bitmap)-> Unit) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    onPhotoTaken(image.toBitmap())

                }
                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("camera","error",exception)
                }
            }
        )
    }

    Scaffold {paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)){
                Row {
                    IconButton(onClick ={ controller.cameraSelector=
                        if(controller.cameraSelector==CameraSelector.DEFAULT_BACK_CAMERA)
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        else CameraSelector.DEFAULT_BACK_CAMERA}){
                        Icon(imageVector = Icons.Default.Cameraswitch, contentDescription = "cameraDiscription")
                    }
                }

            }
            Box (modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)){
                CameraPreview(controller = controller,Modifier.fillMaxSize())
            }
            Box (modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f)){
                Column {

                        Row (horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                                    IconButton(onClick ={ takePhoto(controller,viewModel::onTakePhoto)}){
                                        Icon(imageVector = Icons.Default.Camera, contentDescription = "TakePhoto")
                                    }
                                    val isRecording=viewModel.isRecording.collectAsState()
                                    if(!isRecording.value){
                                        IconButton(onClick ={ viewModel.startRecording(controller,context,navHostController)}){
                                            Icon(imageVector = Icons.Default.Videocam
                                                , contentDescription = "TakePhoto")
                                        }
                                    }
                                    else{
                                        val isRecordingPaused=viewModel.isVideoPaused.collectAsState()
                                        IconButton(onClick ={viewModel.playPauseRecording()}){
                                        Icon(imageVector =
                                            if(isRecordingPaused.value) Icons.Default.PlayArrow
                                            else Icons.Default.Pause
                                            , contentDescription = "TakePhoto"
                                        ) }
                                        IconButton(onClick ={viewModel.finishRecording()}){
                                            Icon(imageVector = Icons.Default.Stop, contentDescription = "TakePhoto")
                                        }
                                    }
                        }
                    val photoList=viewModel.photoList.collectAsState().value
                    LazyRow{
                        items( photoList){bitmap->
                           Image(bitmap = bitmap.asImageBitmap(), contentDescription = null, modifier = Modifier
                               .clip(
                                   RoundedCornerShape(10.dp)
                               )
                               .width(80.dp))
                        }
                    }
                    Button(onClick = { viewModel.addPhoto(navHostController) }) {
                        Text(text = "Right")
                    }
                }
            }
        }
    }
}


@Serializable
object ScreenC