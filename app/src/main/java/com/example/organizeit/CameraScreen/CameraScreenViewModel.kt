package com.example.organizeit.CameraScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.camera.video.FileOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.organizeit.ContentReceivedFromCamera
import com.example.organizeit.showProjects.ui.ScreenA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CameraScreenViewModel @Inject constructor(
):ViewModel() {
    private val _photoList = MutableStateFlow<List<Bitmap>>(emptyList())
    val photoList = _photoList.asStateFlow()


    private val _isRecording = MutableStateFlow(false)
    val isRecording = _isRecording.asStateFlow()
    var recording:Recording?=null
    private val _isVideoPaused=MutableStateFlow(false)
    val isVideoPaused=_isVideoPaused.asStateFlow()
    private val _videoTime=MutableStateFlow(0)
    val videoTime=_videoTime.asStateFlow()


    fun startVideoTimer(){
        val startTime=System.currentTimeMillis()

    }


    fun onTakePhoto(bitmap: Bitmap ){
        _photoList.value+=bitmap
    }
    fun addPhoto(navHostController: NavHostController){
        ContentReceivedFromCamera.CameraPhotoUris=photoList.value
        ContentReceivedFromCamera.availableCRFO()
        navHostController.navigate(ScreenA)

    }

    fun startRecording(controller: LifecycleCameraController,context: Context,navHostController: NavHostController) {
        _isRecording.value = true
        recordVideo(controller,context, navHostController )
    }
    fun finishRecording() {
        Log.d("2","")
        recording?.close()
        recording=null
        _isRecording.value = false
    }
    fun playPauseRecording(){
        if(isVideoPaused.value){
            recording?.resume()
            _isVideoPaused.value=false
        }
        else{
            recording?.pause()
            _isVideoPaused.value=true
        }
    }
    @SuppressLint("MissingPermission")
    fun recordVideo(controller: LifecycleCameraController, context: Context,navHostController: NavHostController){
        if(recording!=null){
            finishRecording()
            return
        }
        val outputFile= File(context.filesDir,"my_recording1"+UUID.randomUUID().toString())
        recording=controller.startRecording(
            FileOutputOptions.Builder(outputFile).build(),
            AudioConfig.create(true),
            ContextCompat.getMainExecutor(context),
        ){event->
            when(event){
                is VideoRecordEvent.Finalize->{
                    if(event.hasError()){
                        finishRecording()
                    }
                    else{
                        _isRecording.value=false
                        ContentReceivedFromCamera.videUri = Uri.parse(event.outputResults.outputUri.toString()).path.toString()
                        ContentReceivedFromCamera.availableCRFO()
                        navHostController.navigate(ScreenA)
                    }
                }
            }

        }
    }

}