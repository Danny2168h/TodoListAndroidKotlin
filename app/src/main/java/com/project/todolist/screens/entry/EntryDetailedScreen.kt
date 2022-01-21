package com.project.todolist.screens.entry

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.project.todolist.MainActivity
import com.project.todolist.R
import com.project.todolist.model.IntToMonth
import com.project.todolist.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat

@ExperimentalComposeUiApi
@Composable
fun EntryDetailedScreen(
    title: String,
    description: String,
    viewModel: EntryDetailedScreenViewModel
) {
    val state by viewModel.state.collectAsState()
    EntryDetailedScreenMain(
        title = title,
        image = state.todoImage,
        dueDate = state.todoDueDate,
        description = description,
        clickReturn = { viewModel.clickReturn() },
        clickSave = { todoTitle, todoDesc, todoImage ->
            viewModel.clickSave(
                todoTitle,
                todoDesc,
                todoImage
            )
        },
        clickDelete = { viewModel.clickDelete() }
    )

}

@ExperimentalComposeUiApi
@Composable
fun EntryDetailedScreenMain(
    title: String,
    image: Bitmap?,
    description: String,
    dueDate: String,
    clickReturn: () -> Unit,
    clickSave: (title: String, description: String, image: Bitmap?) -> Unit,
    clickDelete: () -> Unit
) {
    val tempFile = createImageFile()
    val tempUri = FileProvider.getUriForFile(
        MainActivity.applicationContext(),
        "com.project.todolist.fileprovider",
        tempFile
    )

    var imageUriState by remember { mutableStateOf<Uri?>(null) }
    var tempImage by remember { mutableStateOf<Bitmap?>(null) }
    var newImage by remember { mutableStateOf<Bitmap?>(null) }
    val openPhotoDialog = remember { mutableStateOf(false) }
    val openDeleteDialog = remember { mutableStateOf(false) }

    val selectImageLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
        imageUriState = uri
    }

    val takePhoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            imageUriState = tempUri
        }
    }

    val back = stringResource(R.string.arrow_back)
    val edit = stringResource(R.string.edit)
    val checkSave = stringResource(R.string.check_save)
    val clear = stringResource(R.string.clear_changes)
    val addDesc = stringResource(R.string.add_description_toTodo)

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollTitle = rememberScrollState()
    val scrollDescription = rememberScrollState()
    var titleNotEmpty by remember { mutableStateOf(true) }
    var enabledChangeTitle by remember { mutableStateOf(false) }
    var titleTextState by remember { mutableStateOf(title) }
    var previousTitleState by remember { mutableStateOf(title) }
    var todoDescriptionState by remember { mutableStateOf(description.trim()) }
    var previousDescriptionState by remember { mutableStateOf(description.trim()) }

    TodoListTheme {
        if (openPhotoDialog.value) {
            AlertDialog(onDismissRequest = { openPhotoDialog.value = false },
                title = { Text(text = stringResource(R.string.attach_image)) },
                text = { Text(text = stringResource(R.string.choose_photo_opt)) },
                confirmButton = {
                    Button(
                        onClick = {
                            takePhoto.launch(tempUri)
                            openPhotoDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(BlackAddList),
                    ) {
                        Text(
                            text = stringResource(R.string.from_camera),
                            color = WhiteTextColor
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {
                            selectImageLauncher.launch("image/*")
                            openPhotoDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(BlackAddList),
                    ) {
                        Text(
                            text = stringResource(R.string.from_library),
                            color = WhiteTextColor
                        )
                    }
                }
            )
        }

        if (openDeleteDialog.value) {
            AlertDialog(onDismissRequest = { openDeleteDialog.value = false },
                title = { Text(text = stringResource(R.string.confirm_delete_item)) },
                text = { Text(text = stringResource(R.string.delete_todoItem)) },
                confirmButton = {
                    Button(
                        onClick = {
                            clickDelete()
                            openDeleteDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(BlackAddList),
                    ) {
                        Text(
                            text = stringResource(R.string.yes),
                            color = WhiteTextColor
                        )
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {

                            openDeleteDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(BlackAddList),
                    ) {
                        Text(
                            text = stringResource(R.string.no),
                            color = WhiteTextColor
                        )
                    }
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(ListDetailedViewBackGround)
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.padding(0.dp, 20.dp))
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                if (!enabledChangeTitle) {
                    Icon(
                        Icons.Rounded.ArrowBackIosNew,
                        contentDescription = null,
                        tint = WhiteBackground,
                        modifier = Modifier
                            .size(22.dp)
                            .semantics { testTag = back }
                            .clickable { clickReturn() }
                            .align(Alignment.CenterStart)
                    )
                } else {
                    Icon(
                        Icons.Rounded.Clear,
                        contentDescription = null,
                        tint = WhiteBackground,
                        modifier = Modifier
                            .size(22.dp)
                            .semantics { testTag = clear }
                            .clickable {
                                titleTextState = previousTitleState
                                todoDescriptionState = previousDescriptionState
                                enabledChangeTitle = false
                                imageUriState = null
                                tempImage = null
                            }
                            .align(Alignment.CenterStart))
                }
                Text(
                    text = stringResource(id = R.string.todo_title),
                    color = WhiteTextColor,
                    fontSize = 25.sp,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
                if (enabledChangeTitle) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint = WhiteBackground,
                        modifier = Modifier
                            .size(22.dp)
                            .semantics { testTag = checkSave }
                            .clickable {
                                if (titleNotEmpty) {
                                    newImage = tempImage
                                    clickSave(titleTextState, todoDescriptionState, newImage)
                                    enabledChangeTitle = false
                                    tempImage = null
                                    imageUriState = null
                                }
                            }
                            .align(Alignment.CenterEnd))
                } else {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = null,
                        tint = WhiteBackground,
                        modifier = Modifier
                            .size(22.dp)
                            .semantics { testTag = edit }
                            .clickable {
                                previousTitleState = titleTextState
                                previousDescriptionState = todoDescriptionState
                                enabledChangeTitle = true
                            }
                            .align(Alignment.CenterEnd))
                }
            }
            Spacer(modifier = Modifier.padding(0.dp, 10.dp))
            when {
                enabledChangeTitle -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PhotoSelector(onClickAddImage = {
                            openPhotoDialog.value = true
                        })
                        if (imageUriState != null) {
                            val source = ImageDecoder
                                .createSource(
                                    LocalContext.current.contentResolver,
                                    imageUriState!!
                                )
                            tempImage = ImageDecoder.decodeBitmap(source)
                            imageUriState = null
                        }
                        if (tempImage != null) {
                            Spacer(modifier = Modifier.padding(15.dp, 0.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .height(100.dp)
                            )
                            {
                                Image(bitmap = tempImage!!.asImageBitmap(), contentDescription = "")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                }
                image != null -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        DisplayDueDate(dueDate)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .height(100.dp)
                        )
                        {
                            Image(bitmap = image.asImageBitmap(), contentDescription = "")
                        }
                    }
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                }
                else -> {
                    DisplayDueDate(dueDate)
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!enabledChangeTitle) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 70.dp, maxHeight = 100.dp)
                            .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = titleTextState,
                            fontSize = 20.sp,
                            color = WhiteTextColor,
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 5.dp)
                                .verticalScroll(scrollTitle),
                            textAlign = TextAlign.Start,
                            fontFamily = josefinsans,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 360.dp, maxHeight = 380.dp)
                            .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = if (todoDescriptionState.isEmpty()) {
                                addDesc
                            } else {
                                todoDescriptionState
                            },
                            fontSize = 20.sp,
                            color = if (todoDescriptionState.isEmpty()) {
                                WhiteTextColorFade
                            } else {
                                WhiteTextColor
                            },
                            modifier = Modifier
                                .padding(vertical = 10.dp, horizontal = 10.dp)
                                .verticalScroll(scrollDescription),
                            textAlign = TextAlign.Center,
                            fontFamily = josefinsans,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    OutlinedTextField(
                        value = titleTextState,
                        onValueChange =
                        {
                            titleTextState = it
                            titleNotEmpty = titleTextState.isNotEmpty()
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 70.dp, maxHeight = 120.dp)
                            .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.new_itemTitle),
                                color = WhiteTextColorFade,
                                textAlign = TextAlign.Center,
                                fontFamily = dmSans,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = WhiteTextColor,
                            fontFamily = josefinsans,
                            fontWeight = FontWeight.Bold
                        ),
                        singleLine = false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
                    )
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                    OutlinedTextField(
                        value = todoDescriptionState,
                        onValueChange =
                        {
                            todoDescriptionState = it
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .sizeIn(minHeight = 360.dp, maxHeight = 380.dp)
                            .border(2.dp, WhiteBackground, RoundedCornerShape(20.dp)),
                        placeholder = {
                            Text(
                                text = stringResource(id = R.string.add_description_toTodo),
                                color = WhiteTextColorFade,
                                textAlign = TextAlign.Start,
                                fontFamily = dmSans,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            color = WhiteTextColor,
                            fontFamily = josefinsans,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start
                        ),
                        singleLine = false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                        colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
                    )
                }
                Spacer(modifier = Modifier.padding(vertical = 15.dp))
                if (!enabledChangeTitle) {
                    DeleteItemButton(clickDelete = { openDeleteDialog.value = true })
                }
            }
        }
    }
}

@Composable
fun DisplayDueDate(dueDate: String) {

    Text(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(10.dp),
        text = formatDateString(dueDate),
        fontSize = 15.sp,
        color = BlackTextColor,
        fontFamily = josefinsans,
        fontWeight = FontWeight.Bold
    )

}

@Composable
fun PhotoSelector(onClickAddImage: () -> Unit) {
    val camera = stringResource(R.string.camera_icon)
    val addPhoto = stringResource(R.string.add_photo)
    Box(
        modifier = Modifier
            .shadow(20.dp, RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
            .background(Color.White)
            .clickable { onClickAddImage() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Icon(
                Icons.Rounded.CameraAlt,
                contentDescription = null,
                tint = BlackTextColor,
                modifier = Modifier.semantics { testTag = camera })
            PaddingValues(10.dp, 0.dp)
            Text(
                text = addPhoto,
                color = BlackTextColor,
                fontFamily = dmSans,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DeleteItemButton(
    clickDelete: () -> Unit,
) {
    val trashIcon = stringResource(R.string.delete_forever)
    Box(
        modifier = Modifier
            .shadow(20.dp, RoundedCornerShape(20.dp, 20.dp, 20.dp, 20.dp))
            .background(Color.Red)
            .clickable { clickDelete() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Icon(
                Icons.Rounded.DeleteForever,
                contentDescription = null,
                tint = WhiteTextColor,
                modifier = Modifier.semantics { testTag = trashIcon })
            PaddingValues(10.dp, 0.dp)
            Text(
                text = stringResource(id = R.string.delete),
                color = WhiteTextColor,
                fontFamily = dmSans,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun createImageFile(): File {
    val folder = MainActivity.applicationContext().cacheDir
    val newFile = File("${MainActivity.applicationContext().cacheDir}/temp.jpg")
    newFile.createNewFile()
    val file: File? = folder.listFiles().find { it.name == "temp.jpg" }
    return file!!
}


fun formatDateString(
    dateString: String
): String {
    if (dateString == "") {
        return ""
    }
    val dueDate = SimpleDateFormat("dd/MM/yyyy HH:mm").parse(dateString)
    val month = dueDate.month
    val year = (dueDate.year + 1900) % 100
    val minute = dueDate.minutes
    val hour = dueDate.hours
    val day = dueDate.date

    return "$day/${IntToMonth.convertIntMonthToString(month)}/$year " +
            "${if (hour > 12) hour - 12 else if (hour == 0) "12" else hour}:" +
            "${if (minute < 10) "0$minute" else minute}${if (hour >= 12) "PM" else "AM"}"
}



