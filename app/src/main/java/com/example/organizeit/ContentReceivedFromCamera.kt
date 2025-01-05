package com.example.organizeit

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ContentReceivedFromCamera {
    var CameraPhotoUris: List<Bitmap> = emptyList()
    var videUri:String=""


    private val _isCRFCAvailable= MutableStateFlow(false)
    val isCRFCAvailable=_isCRFCAvailable.asStateFlow()

    fun clear(){
        CameraPhotoUris= emptyList()
        videUri=""
        _isCRFCAvailable.value = false

    }
    fun availableCRFO(){
        _isCRFCAvailable.value=true
    }

}