package com.example.organizeit

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object ContentReceivedFromOutsideApp {
    var sharedFileUri: Uri? = null
    var sharedFileUris: List<Uri> = emptyList()
    private val _isCRFOAvailable=MutableStateFlow(false)
    val isCRFOAvailable=_isCRFOAvailable.asStateFlow()

    fun clear(){
        sharedFileUris= emptyList()
        sharedFileUri=null
        _isCRFOAvailable.value = false

    }
    fun availableCRFO(){
        _isCRFOAvailable.value=true
    }


}