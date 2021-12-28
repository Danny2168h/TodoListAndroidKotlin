package com.project.todolist.screens.entry

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
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
import com.project.todolist.R
import com.project.todolist.model.IntToMonth
import com.project.todolist.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun EntryDetailedScreen(
    title: String,
    description: String,
    viewModel: EntryDetailedScreenViewModel
) {
    val state by viewModel.state.collectAsState()
    EntryDetailedScreen().EntryDetailedScreenMain(
        title = title,
        image = state.todoImage,
        dueDate = state.todoDueDate,
        description = description,
        clickReturn = { viewModel.clickReturn() },
        clickSave = { title, description, image -> viewModel.clickSave(title, description, image) },
        clickDelete = { viewModel.clickDelete() }
    )

}

class EntryDetailedScreen {

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

        var noNewSaved by remember { mutableStateOf(true) }

        var imageUriState by remember { mutableStateOf<Uri?>(null) }
        var newImage by remember { mutableStateOf<Bitmap?>(null) }
        var tempImage by remember { mutableStateOf<Bitmap?>(null) }
        var alreadyDecoded by remember {mutableStateOf(false)}

        println("$imageUriState        $newImage          $tempImage")

        val selectImageLauncher = rememberLauncherForActivityResult(GetContent()) { uri: Uri? ->
            imageUriState = uri
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(ListDetailedViewBackGround),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.padding(0.dp, 20.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    if (!enabledChangeTitle) {
                        Icon(
                            Icons.Rounded.ArrowBackIosNew,
                            contentDescription = null,
                            tint = WhiteBackground,
                            modifier = Modifier
                                .size(22.dp)
                                .semantics { testTag = back }
                                .clickable { clickReturn() })
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
                                    alreadyDecoded = false
                                })
                    }
                    Text(
                        text = stringResource(id = R.string.todo_title),
                        color = WhiteTextColor,
                        fontSize = 25.sp,
                        fontFamily = montserrat,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(250.dp)
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
                                        tempImage = null
                                        enabledChangeTitle = false
                                        noNewSaved = false
                                        clickSave(titleTextState, todoDescriptionState, newImage)
                                        alreadyDecoded = false
                                    }
                                })
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
                                })
                    }
                }
                Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                if (enabledChangeTitle) {
                    Row(
                        modifier = Modifier.width(350.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PhotoSelector(onClickAddImage = { selectImageLauncher.launch("image/*") })
                        if (imageUriState != null && !alreadyDecoded) {
                            val source = ImageDecoder
                                .createSource(
                                    LocalContext.current.contentResolver,
                                    imageUriState!!
                                )
                            tempImage = ImageDecoder.decodeBitmap(source)
                            alreadyDecoded = true
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
                } else if (newImage != null || image != null) {
                    Row(modifier = Modifier.width(350.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        DisplayDueDate(dueDate)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .height(100.dp)
                        )
                        {
                            if (image != null && noNewSaved) {
                                Image(bitmap = image!!.asImageBitmap(), contentDescription = "")
                            } else {
                                Image(bitmap = newImage!!.asImageBitmap(), contentDescription = "")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
                } else {
                    DisplayDueDate(dueDate)
                    Spacer(modifier = Modifier.padding(0.dp, 10.dp))
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
                                .width(350.dp)
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
                                .width(350.dp)
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
                                .width(350.dp)
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
                                .width(350.dp)
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
                                fontWeight = FontWeight.Bold
                            ),
                            singleLine = false,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                            colors = TextFieldDefaults.textFieldColors(textColor = WhiteTextColor),
                        )
                    }
                    Spacer(modifier = Modifier.padding(vertical = 15.dp))
                    if (!enabledChangeTitle) {
                        DeleteItemButton(clickDelete = { clickDelete() })
                    }
                }
            }
        }
    }

    @Composable
    fun DisplayDueDate(dueDate: String) {

        Text(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color.White).padding(10.dp),
            text = formatDateString(dueDate),
            fontSize = 15.sp,
            color = BlackTextColor,
            fontFamily = josefinsans,
            fontWeight = FontWeight.Bold)

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



