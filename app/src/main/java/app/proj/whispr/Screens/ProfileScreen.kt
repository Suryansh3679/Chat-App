package app.proj.whispr.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import app.proj.whispr.CommonProgressBar
import app.proj.whispr.LCViewModel


@Composable
fun ProfileScreen(navController: NavController, viewModel: LCViewModel) {
    val inProgress = viewModel.inProgress.value
    if (inProgress){
        CommonProgressBar()
    }else{
        Column {
            ProfileContent()
            BottomNavigationMenu(
                selectedItem = BottomNavigationMenuItem.PROFILE,
                navController = navController
            )
        }
    }


}

@Composable
fun ProfileContent(){

    Column {
        Row {

        }
    }

}