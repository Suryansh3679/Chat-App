package app.proj.whispr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController


fun navigateTo(navController: NavController,route : String){

    navController.navigate(route){
        popUpTo(route)
        launchSingleTop = true
    }
}


@Composable
fun CommonProgressBar(){

    Row (modifier = Modifier
        .alpha(0.5f)
        .background(Color.LightGray)
        .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        CircularProgressIndicator()
    }
}