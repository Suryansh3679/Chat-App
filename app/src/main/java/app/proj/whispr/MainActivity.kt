package app.proj.whispr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.proj.whispr.Screens.ChatListScreen
import app.proj.whispr.Screens.LoginScreen
import app.proj.whispr.Screens.SignUpScreen
import app.proj.whispr.ui.theme.WhisprTheme
import dagger.hilt.android.AndroidEntryPoint

sealed class DestinationScreen(var route: String) {

    object SignUp : DestinationScreen("signup")
    object Login : DestinationScreen("login")
    object Profile : DestinationScreen("profile")
    object ChatList : DestinationScreen("chatList")
    object SingleChat : DestinationScreen("singleChat/{chatId}") {
        fun createRoute(id: String) = "singleChat/$id"
    }

    object StatusList : DestinationScreen("StatusList")
    object SingleStatus : DestinationScreen("singleStatus/{userId}") {
        fun createRoute(userid: String) = "singleChat/$userid"
    }

}


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WhisprTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatAppNavigation()
                }
            }
        }
    }

    @Composable
    fun ChatAppNavigation() {

        val navController = rememberNavController()
        val viewModel = hiltViewModel<LCViewModel>()

        NavHost(navController = navController, startDestination = DestinationScreen.SignUp.route) {

            composable(DestinationScreen.SignUp.route) {

                SignUpScreen(navController,viewModel)
            }
            composable(DestinationScreen.Login.route) {
                LoginScreen(viewModel,navController)
            }

            composable(DestinationScreen.ChatList.route) {
                ChatListScreen()
            }


        }


    }
}

