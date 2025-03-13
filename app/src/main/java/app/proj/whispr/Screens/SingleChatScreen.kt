package app.proj.whispr.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import app.proj.whispr.LCViewModel


@Composable
fun SingleChatScreen(navController: NavController, viewModel: LCViewModel,chatId :String){

    Text(text = chatId)

}