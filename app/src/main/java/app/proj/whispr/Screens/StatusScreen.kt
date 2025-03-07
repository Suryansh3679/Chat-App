package app.proj.whispr.Screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import app.proj.whispr.LCViewModel


@Composable
fun StatusScreen(navController: NavController, viewModel: LCViewModel){

    BottomNavigationMenu(selectedItem = BottomNavigationMenuItem.STATUSLIST, navController = navController )
}