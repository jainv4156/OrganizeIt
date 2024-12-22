package com.example.organizeit.contentScreen.document

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap

fun documentOpener(  item: Uri, context: Context) {
    val memeType=getMimeTypeFromUri(context.contentResolver,item)


    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(item,memeType)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

fun getMimeTypeFromUri(contentResolver: ContentResolver, uri: Uri): String? {
    // Check if the URI has a content scheme (used when picking from storage)
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        return contentResolver.getType(uri)
    } else {
        // If the URI has a file scheme, get the file extension and determine the MIME type
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.lowercase())
    }
}