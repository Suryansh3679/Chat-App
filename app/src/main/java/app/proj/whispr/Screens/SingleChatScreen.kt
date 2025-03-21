package app.proj.whispr.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.CommonDivider
import app.proj.whispr.CommonImage
import app.proj.whispr.LCViewModel
import app.proj.whispr.data.Message


@Composable
fun SingleChatScreen(navController: NavController, viewModel: LCViewModel, chatId: String) {
    var reply by rememberSaveable {
        mutableStateOf("")
    }
    val onSendReply = {
        viewModel.onSendReply(chatId, reply)
        reply = ""
    }
    val myUser = viewModel.userData.value
    val currentChat = viewModel.chats.value.first {
        it.chatId == chatId
    }
    val chatMessages = viewModel.chatMessages
    val chatUser =
        if (currentChat.user1.userId != myUser?.userId) currentChat.user1 else currentChat.user2

    LaunchedEffect(key1 = Unit) {
        viewModel.populateMessages(chatId)
    }
    BackHandler {
        viewModel.dePopulateMessages()
    }

    Scaffold(
        bottomBar = {
            ReplyBox(
                reply = reply,
                onReplyChange = { reply = it },
                onSendReply = onSendReply
            )
        }
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(it)) {
            ChatHeader(name = chatUser.name.toString(), imageUrl = chatUser.imageUrl ?: "") {
                navController.navigateUp()
                viewModel.dePopulateMessages()
            }

            MessageBox(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                chatMessages = chatMessages.value,
                currentUserId = myUser?.userId.toString(),
                navController=navController
            )
        }
    }
}
@Composable
fun MessageBox(modifier: Modifier, chatMessages: List<Message>, currentUserId: String,navController: NavController) {
    val listState = rememberLazyListState()

    // Auto-scroll to the last message when messages update
    LaunchedEffect(chatMessages) {
        if (chatMessages.isNotEmpty()) {
            listState.scrollToItem(chatMessages.size-1)  // Scroll to the last item (index 0 in reverse mode)
        }
    }
    BackHandler {
        navController.navigateUp()
    }
    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
    ) {
        items(chatMessages) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color = if (msg.sendBy == currentUserId) Color(0xFF68C400) else Color(0xFFC0C0C0)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalAlignment = alignment
            ) {
                Text(
                    text = msg.message.toString(),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                    )
            }
        }
    }

}

@Composable
fun ChatHeader(name: String, imageUrl: String? = "", onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClicked() }
                .padding(8.dp)
        )
        CommonImage(
            data = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(50.dp)
                .clip(CircleShape)
        )
        Text(text = name, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))

    }

}

@Composable
fun ReplyBox(reply: String, onReplyChange: (String) -> Unit, onSendReply: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        CommonDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                modifier = Modifier.width(240.dp),
                value = reply,
                onValueChange = onReplyChange,
                maxLines = 3
            )
            Spacer(modifier = Modifier.padding(7.dp))
            Button(onClick = onSendReply) {
                Text(text = "Send")
            }
        }

    }
}