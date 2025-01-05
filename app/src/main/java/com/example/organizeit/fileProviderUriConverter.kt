package com.example.organizeit

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File


fun fileProviderUriConverter(context: Context, uri: Uri?): Uri? {
    val file= File(uri.toString())
    val fileProviderUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider", // Your FileProvider authority
        file
    )
    return fileProviderUri
}
