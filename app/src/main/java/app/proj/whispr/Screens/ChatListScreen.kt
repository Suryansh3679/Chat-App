package app.proj.whispr.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.CommonProgressBar
import app.proj.whispr.CommonRow
import app.proj.whispr.DestinationScreen
import app.proj.whispr.LCViewModel
import app.proj.whispr.TitleText
import app.proj.whispr.navigateTo


@Composable
fun ChatListScreen(navController: NavController, viewModel: LCViewModel) {

    val inProgress = viewModel.inProcessChat.value

    if (inProgress) {
        CommonProgressBar()
    } else {
        val chats = viewModel.chats.value
        val userData = viewModel.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            viewModel.onAddChat(it)


            showDialog.value = false
        }
        Scaffold(
            floatingActionButton = {
                FAB(
                    showDialog = showDialog.value,
                    onFabClick = { onFabClick() },
                    onDismiss = { onDismiss() },
                    onAddChat = onAddChat
                )
            },
            bottomBar = {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationMenuItem.CHATLIST,
                    navController = navController
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            )
            {
                TitleText(txt = "Chats")

                if(chats.isEmpty()){
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                        Text(text = "No Chats Available")
                    }
                }else{
                    LazyColumn(modifier = Modifier.weight(1f)){
                        items(chats){
                            chat->
                            val chatUser = if(chat.user1.userId == userData?.userId){
                                chat.user2
                            }else{
                                chat.user1
                            }
                            CommonRow(imageUrl = chatUser.imageUrl?:"", name = chatUser.name) {

                                chatUser.userId?.let{
                                    navigateTo(navController,DestinationScreen.SingleChat.createRoute(id = chat.chatId))
                                }
                            }
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun FAB(
    showDialog: Boolean,
    onFabClick: () -> Unit,
    onDismiss: () -> Unit,
    onAddChat: (String) -> Unit
) {

    val addChatNumber = remember {
        mutableStateOf("")
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                addChatNumber.value = ""
            },
            confirmButton = {
                Button(onClick = { onAddChat(addChatNumber.value) }) {
                    Text(text = "Add Chat")
                }

            },
            title = { Text(text = "Add Char") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value, onValueChange = {
                        addChatNumber.value = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        )
    } else {
        FloatingActionButton(
            onClick = { onFabClick() },
            containerColor = MaterialTheme.colorScheme.secondary,
            shape = CircleShape,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = null, tint = Color.White)
        }
    }
}