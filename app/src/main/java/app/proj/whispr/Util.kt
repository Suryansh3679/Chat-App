package app.proj.whispr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter


fun navigateTo(navController: NavController, route: String) {

    navController.navigate(route) {
        popUpTo(route)
        launchSingleTop = true
    }
}


@Composable
fun CommonProgressBar() {

    Row(
        modifier = Modifier
            .alpha(0.5f)
            .background(Color.LightGray)
            .fillMaxSize()
            .clickable(enabled = false) {},
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    )
    {
        CircularProgressIndicator(modifier = Modifier.size(50.dp))
    }
}

@Composable
fun CommonDivider() {
    HorizontalDivider(
        color = Color.Black, thickness = 3.dp,
        modifier = Modifier
            .alpha(0.3f)
            .padding(vertical = 8.dp)
    )

}

@Composable
fun CommonImage(
    data : String?,
    modifier: Modifier = Modifier.wrapContentSize(),
    contentScaling : ContentScale = ContentScale.Crop
){
    val painter = rememberAsyncImagePainter(model = data)
    Image(painter = painter, contentDescription = "",modifier = modifier,
        contentScale = contentScaling)

}

@Composable
fun CheckSignedIn(vm: LCViewModel, navController: NavController) {
    val alreadySignedIn = remember { mutableStateOf(false) }

    val signIn = vm.signIn.value

    if (signIn && !alreadySignedIn.value) {
        navController.navigate(DestinationScreen.ChatList.route) {
            popUpTo(0)
        }
    }
}