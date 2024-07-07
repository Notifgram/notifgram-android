package com.notifgram.fcm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.notifgram.core.common.MyLog.i
import com.notifgram.core.presentation_core.theme.notifgramTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject
import javax.inject.Named

class UIState {
    var textMessageContent: String = "empty"

    var globalFileName: String = "b.jpg"
    val folderName: String
        get() = globalFileName.replace('.', '-')

}

var state by mutableStateOf(UIState())

@HiltViewModel
class CachedFilesScreenViewModel @Inject constructor(
    @Named("CachedFilesDir") val cachedFilesDir: File
) : ViewModel()


@Composable
fun CachedFilesScreen(
    viewModel: CachedFilesScreenViewModel = hiltViewModel()
) {
    val filesDir = LocalContext.current.filesDir // Get a reference to the app's files directory
    val cachedFilesDir = viewModel.cachedFilesDir// Get a reference to the cached files directory

    var fileContent by remember { mutableStateOf("") }
    var fileContentDecrypted by remember { mutableStateOf("") }

    val onFileClick = { file: File ->
        if (file.isFile) {
            fileContent = file.readText()
//            fileContentDecrypted=
        }
    }


    var allFiles = filesDir.listFiles()?.toMutableList()
    Column {
        if (allFiles != null)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                if (allFiles!!.isNotEmpty()) {
                    items(allFiles!!.count()) {
                        val file = allFiles!![it]
                        Text(file.absolutePath, Modifier.clickable { onFileClick(file) })
                        HorizontalDivider()
                        if (file.isDirectory) {
                            file.listFiles()?.forEach { subDirectory ->
                                Text(
                                    subDirectory.absolutePath,
                                    Modifier.clickable { onFileClick(subDirectory) })
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        Button(onClick = {
            allFiles?.clear()
            allFiles = filesDir.listFiles()?.toMutableList()
        }) {
            Text("show files")
        }

        Text("Text Received=", Modifier.background(Color.Gray))
        Text(state.textMessageContent, Modifier.background(Color.Gray))

        var isDeleted by remember { mutableStateOf(false) }
        Button(onClick = {
            isDeleted = cachedFilesDir.deleteRecursively()
            allFiles?.clear()
            allFiles = filesDir.listFiles()?.toMutableList()
        }) {
            Text("Delete cachedFilesDir folder")
        }
        Text("is deleted=$isDeleted")

        Button(onClick = {
            isDeleted = filesDir.deleteRecursively()
            allFiles?.clear()
            allFiles = filesDir.listFiles()?.toMutableList()
        }) {
            Text("Delete filesDir folder")
        }

//        Button(onClick = {
//            val file = File(filesDir.path.plus("/a-txt"), "a.txt")
//            fileContent = file.readText()
//            if (ShouldEncrypt) {
//                var fileEncryptionKey: SecretKey? = null
//                getKeyStore()?.let {
//                    fileEncryptionKey = readKeyFromKeystore(FILE_ENCRYPTION_KEY_ALIAS, it)
//                }
//
//                fileContentDecrypted = decryptByteArray(
//                    file.readBytes(),
//                    fileEncryptionKey!!
//                ).decodeToString()
//            }
//        }) {
//            Text("Open file")
//        }
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Text(text = "File contents read from storage=\n $fileContent")
            Text(
                text = "File contents decrypted count=\n ${fileContentDecrypted.count()}",
                fontWeight = FontWeight.Bold
            )
            Text(text = "File contents decrypted=\n $fileContentDecrypted")
        }


        var imageFileContent: ByteArray? by remember { mutableStateOf(null) }

        Button(onClick = {

//            imageFileContent = if (ShouldEncrypt) {
//                var fileEncryptionKey: SecretKey? = null
//                getKeyStore()?.let {
//                    fileEncryptionKey = readKeyFromKeystore(FILE_ENCRYPTION_KEY_ALIAS, it)
//                }
//                decryptByteArray(
//                    file.readBytes(),
//                    fileEncryptionKey!!
//                )
//            }
//            else
//                file.readBytes()

//            val decryptedFile = File(filesDir.path.plus("/$folderName"), "b-decrypted.jpg")
//            writeToFile(decryptedFile, imageFileContentDecrypted!!) // TODO: delete
//            i("file hash1=${getFileHash(decryptedFile)}") // TODO: delete
            i("onButtonClick imageFileContentDecrypted=${imageFileContent.toString()}") // TODO: delete
        }) {
            Text("Open image")
        }

//        val decryptedFile = File(filesDir.path.plus("/b-jpg"), "b-decrypted.jpg")
//        if(decryptedFile.exists()) {
//            i("file hash2=${getFileHash(decryptedFile)}")
//            imageFileContentDecrypted = decryptedFile.readBytes()
//            i(" imageFileContentDecrypted=${imageFileContentDecrypted.toString()}")
//        }

//        if (imageFileContentDecrypted != null) {
//            ByteArrayToImageBitmap(imageFileContentDecrypted!!)
//        val bitmap = BitmapFactory.decodeByteArray(image/**/FileContentDecrypted, 0, imageFileContentDecrypted!!.size)
//        val imageBitmap = ImageBitmap.bitmap(bitmap.asImageAsset())

//            var bmp = ImageBitmapFactory.decodeByteArray(
//                imageFileContentDecrypted, 0,  imageFileContentDecrypted!!.size)

        val imageFileName: String = "1.jpg"
        val folderName: String = imageFileName.replace('.', '-')

        val file = File(filesDir.path.plus("/${folderName}"), imageFileName)
        if (file.exists()) {
//            val decryptedData =
//                readKeyFromKeystore()?.let { FileEncryptionUtils.decryptByteArray(file.readBytes(), it) }
//            if (decryptedData != null)
////                ImageViewer(imageContent = byteArrayToImageBitmap(decryptedData))
//            else
//                e("decryptedData is null")
        }
//            Image(
//                bitmap = byteArrayToImageBitmap(imageFileContent!!),
//                contentDescription = "123",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//            )

//            CoilImage(
//
//                imageRequest = {byteArrayToImageBitmap(imageFileContentDecrypted!!)},
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//            )
//        }


    }
}

//@Composable
//fun ByteArrayToImageBitmap(byteArray: ByteArray) {
//    val context = LocalContext.current
//    val imageLoader = ImageLoader(context)
//
//    val bitmapPainter: Painter = remember(byteArray) {
//        val imageRequest = ImageRequest.Builder(context)
//            .data(byteArray)
//            .build()
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val result = imageLoader.execute(imageRequest)
//            if (result is SuccessResult) {
//                withContext(Dispatchers.Main) {
//                    result.drawable.toBitmap().asImageBitmap()
//                }
//            }
//        }
//
//        BitmapPainter(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).asImageBitmap())
//    }
//
//    Image(painter = bitmapPainter, contentDescription = "Image")
//}


@Preview
@Composable
fun TestScreenPreview() {
    notifgramTheme {
        CachedFilesScreen()
    }

}