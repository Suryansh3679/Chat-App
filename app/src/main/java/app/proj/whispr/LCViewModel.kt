package app.proj.whispr

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import app.proj.whispr.data.Events
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.events.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth


) : ViewModel() {

    var inProgress = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)

    fun signUp(context:Context,name: String,number: String,email: String,password: String){

        inProgress.value =true

        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            Log.e("TAGit", "signup : Password must be at least 6 characters")
            return
        }
        auth.createUserWithEmailAndPassword(email,password) .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAGit", "signup : User Logged In")
            } else {

            }
        }
    }

    fun handleException(exception: Exception?= null, customMessage : String = ""){

        Log.e("TAGit", "signup : Error -> ",exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage?:""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutableState.value = Events(message)
        inProgress.value = false


    }
}
