package app.proj.whispr.Screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.proj.whispr.CommonDivider
import app.proj.whispr.CommonImage
import app.proj.whispr.CommonProgressBar
import app.proj.whispr.LCViewModel


@Composable
fun ProfileScreen(navController: NavController, viewModel: LCViewModel) {
    val inProgress = viewModel.inProgress.value
    if (inProgress) {
        CommonProgressBar()
    } else {
        Column {
            ProfileContent({}, {}, viewModel)
            BottomNavigationMenu(
                selectedItem = BottomNavigationMenuItem.PROFILE,
                navController = navController
            )
        }
    }
}

@Composable
fun ProfileContent(onBack: () -> Unit, onSave: () -> Unit,
    viewModel: LCViewModel
) {
    val imageUrl = viewModel.userData.value?.imageUrl

    Column {
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
        ProfileImage(imageUrl = imageUrl,viewModel = viewModel)

        CommonDivider()


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
                    launcher.launch("images/*")
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
            ) {
                CommonImage(data = imageUrl)
            }
            Text(text = "Change Profile Image")
        }
        if (viewModel.inProgress.value){
            CommonProgressBar()
        }
    }

}








