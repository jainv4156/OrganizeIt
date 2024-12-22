package com.example.organizeit.contentScreen.data

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VideoOnly


val mimeType= arrayOf(
    "application/pdf", // PDF
    "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // DOC, DOCX
    "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // XLS, XLSX
    "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // PPT, PPT
)
enum class ProjectContentSelectedLauncher{

    PhotoPickerLauncher {
        override fun performAction(launcher: ContentSelectedLauncher) {
            val photoPickerLauncher = launcher as ContentSelectedLauncher.PhotoPickerLauncher
            photoPickerLauncher.photoPickerLauncher.launch(
                PickVisualMediaRequest(ImageOnly)
            )
        }
    },
    AudioPickerLauncher{
        override fun performAction(launcher: ContentSelectedLauncher) {
            val audioPickerLauncher = launcher as ContentSelectedLauncher.AudioPickerLauncher
            audioPickerLauncher.audioPickerLauncher.launch(
                "audio/*"
            )
        }
    },

    VideoPickerLauncher{
        override fun performAction(launcher: ContentSelectedLauncher) {
            val videoPickerLauncher=launcher as ContentSelectedLauncher.VideoPickerLauncher
            videoPickerLauncher.videoPickerLauncher.launch(
                PickVisualMediaRequest(VideoOnly))
        }
    },
    DocumentPickerLauncher{
        override fun performAction(launcher: ContentSelectedLauncher) {
            val documentPickerLauncher=launcher as ContentSelectedLauncher.DocumentPickerLauncher
            documentPickerLauncher.documentPickerLauncher.launch(
                mimeType)
        }
    };

    abstract fun performAction(launcher:ContentSelectedLauncher)
    }

sealed class ContentSelectedLauncher{

    class PhotoPickerLauncher(val photoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>):ContentSelectedLauncher()
     class AudioPickerLauncher(val audioPickerLauncher: ManagedActivityResultLauncher<String, List<@JvmSuppressWildcards Uri>>):ContentSelectedLauncher()
     class VideoPickerLauncher(val videoPickerLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, List<@JvmSuppressWildcards Uri>>):ContentSelectedLauncher()
     class DocumentPickerLauncher(val documentPickerLauncher: ManagedActivityResultLauncher<Array<String>, List<@JvmSuppressWildcards Uri>>):ContentSelectedLauncher()
}