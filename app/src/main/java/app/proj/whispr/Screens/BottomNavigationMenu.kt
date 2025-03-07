package app.proj.whispr.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.DestinationScreen
import app.proj.whispr.R
import app.proj.whispr.navigateTo


enum class BottomNavigationMenuItem(val icon: Int, val navDestinationScreen: DestinationScreen) {

    CHATLIST(R.drawable.chat, DestinationScreen.ChatList),
    STATUSLIST(R.drawable.status, DestinationScreen.StatusList),
    PROFILE(R.drawable.profile, DestinationScreen.Profile)

}

@Composable
fun BottomNavigationMenu(
    selectedItem: BottomNavigationMenuItem,
    navController: NavController
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 4.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        for (items in BottomNavigationMenuItem.values()) {
            Box (
                modifier = Modifier
                    .weight(1f) // Distributes equal space among items
                    .fillMaxWidth() // Ensures each takes its space
                    .padding(horizontal = 8.dp)
                    .weight(1f)
                    .clickable {
                        navigateTo(navController, items.navDestinationScreen.route)
                    }
                    .background(if (items == selectedItem) colorResource(id = R.color.light_green)
                        else Color.White
                    )
            ,
                contentAlignment = Alignment.Center
            ){
                Image(painter = painterResource(id = items.icon), contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
//

//                        .background((if (items == selectedItem) colorResource(id = R.color.light_green)
//                        else Color.White))
                    ,
//                    colorFilter = if (items == selectedItem) ColorFilter.tint(Color.Black)
//                    else ColorFilter.tint(Color.Gray)

                )
            }


        }

    }
}