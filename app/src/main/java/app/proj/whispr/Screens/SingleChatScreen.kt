package app.proj.whispr.Screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.CommonDivider
import app.proj.whispr.CommonImage
import app.proj.whispr.LCViewModel
import javax.annotation.Untainted
import kotlin.math.log


@Composable
fun SingleChatScreen(navController: NavController, viewModel: LCViewModel, chatId: String) {

    var reply by rememberSaveable {
        mutableStateOf("")
    }


    val onSendReply = {
        viewModel.onSendReply(chatId, reply)
        reply = ""
    }
    Column {
        ChatHeader(name = viewModel.userData.value?.name.toString(), imageurl = viewModel.userData.value?.imageUrl.toString()) {
            navController.navigateUp()
        }

        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)

    }

}

@Composable
fun ChatHeader(name: String, imageurl: String? = "", onBackClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClicked() }
                .padding(8.dp)
        )
        CommonImage(
            data = imageurl,
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