package com.example.organizeit.contentScreen.uiComponents.contentView


import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.organizeit.contentScreen.ContentScreenViewModel
import com.example.organizeit.contentScreen.data.ContentSelectedLauncher
import com.example.organizeit.contentScreen.data.ProjectContentSelectedLauncher
import com.example.organizeit.contentScreen.data.ProjectContentTypeString
import com.example.organizeit.contentScreen.document.documentOpener
import kotlinx.coroutines.launch

@Composable
fun DocumentView(projectFolderId:String) {
    val viewModel= hiltViewModel<ContentScreenViewModel>()
    val scope= rememberCoroutineScope()

    val documentPickerLauncher=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = {
            scope.launch {
                viewModel.addContent(id=projectFolderId, uri = it)
            }
        }
    )

    val contentUri=viewModel.selectedContentUri.collectAsState()
    val context: Context = LocalContext.current

    LazyColumn {
        item{
            Button(onClick = {ProjectContentSelectedLauncher.DocumentPickerLauncher.performAction(ContentSelectedLauncher.DocumentPickerLauncher(documentPickerLauncher)) }) {
                Text(text = "ClickME")
            }}
        items(contentUri.value.filter { it.type == ProjectContentTypeString.Documents.value }){ item->
            Text(text = item.toString()
                ,modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        documentOpener(Uri.parse(item.uri), context)
                    })
        }
    }

}