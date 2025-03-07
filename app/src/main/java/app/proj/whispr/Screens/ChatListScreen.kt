package app.proj.whispr.Screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import app.proj.whispr.LCViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun ChatListScreen(navController: NavController,viewModel: LCViewModel){


    BottomNavigationMenu(selectedItem = BottomNavigationMenuItem.CHATLIST, navController = navController )
}