package app.proj.whispr.Screens

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.CommonProgressBar
import app.proj.whispr.LCViewModel
import app.proj.whispr.TitleText


@Composable
fun StatusScreen(navController: NavController, viewModel: LCViewModel) {
    val inProcess = viewModel.inProgressStatus.value
    if (inProcess) {
        CommonProgressBar()
    } else {
        val status = viewModel.status.value
        val userData = viewModel.userData.value
        val myStatuses = status.filter {
            it.user.userId == userData?.userId
        }
        val otherStatuses = status.filter {
            it.user.userId != userData?.userId
        }

        Scaffold(
            floatingActionButton = {
                FAB {

                }
            },
            content = {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(it)) {
                    TitleText(txt = "Status")
                    if(status.isEmpty()){
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "No Status Available")
                        }
                    }else{

                    }
                }
            }
        )

        BottomNavigationMenu(
            selectedItem = BottomNavigationMenuItem.STATUSLIST,
            navController = navController
        )
    }

}


@Composable
fun FAB(
    onFabClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onFabClick,
        containerColor = MaterialTheme.colorScheme.secondary,
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Edit,
            contentDescription = "Add Status",
            tint = Color.White
        )
    }

}