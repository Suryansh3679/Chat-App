package app.proj.whispr.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.CommonDivider
import app.proj.whispr.CommonProgressBar
import app.proj.whispr.DestinationScreen
import app.proj.whispr.LCViewModel
import app.proj.whispr.R


//@Composable
@Composable
fun ProfileScreen(navController: NavController, viewModel: LCViewModel) {
    val inProgress = viewModel.inProgress.value
    val keyboardController = LocalSoftwareKeyboardController.current
    if (inProgress) {
        CommonProgressBar()
    } else {
        Scaffold(
            bottomBar = {
                BottomNavigationMenu(
                    selectedItem = BottomNavigationMenuItem.PROFILE,
                    navController = navController
                )
            }
        ) { paddingValues -> // Ensures content does not overlap bottom bar
            val userData = viewModel.userData.value
            var name by rememberSaveable {
                mutableStateOf(userData?.name?:"")
            }
            var number by rememberSaveable {
                mutableStateOf(userData?.number?:"")
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues) // Adjust padding to avoid overlap
                    .verticalScroll(rememberScrollState())
            ) {
                ProfileContent(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    onBack = {
                             navController.navigate(DestinationScreen.ChatList.route)

                    },
                    onSave = {
                             viewModel.createOrUpdateProfile(
                                 name = name,
                                 number = number
                             )
                        keyboardController?.hide()
                    },
                    viewModel = viewModel,
                    name = name,
                    number = number,
                    onNameChange = {name = it},
                    onNumberChange = {number = it},
                    onLogOut = {
                        viewModel.logOut()
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    modifier: Modifier,
    onBack: () -> Unit,
    onSave: () -> Unit,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit,
    onLogOut: () -> Unit,
    viewModel: LCViewModel
) {
    val imageUrl = viewModel.userData.value?.imageUrl

    Column(

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", Modifier.clickable {
                onBack.invoke()
            })
            Text(text = "Save", Modifier.clickable {
                onSave.invoke()
            })
        }
        CommonDivider()
        ProfileImage(imageUrl = imageUrl, viewModel = viewModel)

        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Name", modifier = Modifier.width(100.dp))
            TextField(
                value = name, onValueChange = onNameChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.LightGray
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Number", modifier = Modifier.width(100.dp))
            TextField(
                value = number, onValueChange = onNumberChange,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedContainerColor = Color.LightGray
                )
            )
        }
        CommonDivider()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Log Out", modifier = Modifier.clickable { onLogOut.invoke() })
        }

    }

}

@Composable
fun ProfileImage(imageUrl: String?, viewModel: LCViewModel) {

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            viewModel.uploadProfileImage(uri)
        }


    }
    Box(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Min)) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    launcher.launch("image/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.profile), contentDescription = null)
//                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Image")
        }
        if (viewModel.inProgress.value) {
            CommonProgressBar()
        }
    }

}








