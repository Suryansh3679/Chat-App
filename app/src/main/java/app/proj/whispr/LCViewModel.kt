package app.proj.whispr

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import app.proj.whispr.data.Events
import app.proj.whispr.data.USER_NODE
import app.proj.whispr.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.events.Event
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth,
    val db : FirebaseFirestore

) : ViewModel() {
    //to show circular progress Bar
    var inProgress = mutableStateOf(false)
    val eventMutableState = mutableStateOf<Events<String>?>(null)
    // To check that user successfully signed in or not
    var signIn = mutableStateOf(false)
    // If user already has a account
    var userData = mutableStateOf<UserData?>(null)

    fun signUp(context: Context, name: String, number: String, email: String, password: String) {

        inProgress.value = true

        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                .show()
            Log.e("TAGit", "signup : Password must be at least 6 characters")
            return
        }
        //If email has useless spaces
        val trimmedEmail = email.trim()

        auth.createUserWithEmailAndPassword(trimmedEmail, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAGit", "signup : User Logged In")

                signIn.value = true
                createOrUpdateProfile(name,number)

            } else {
                handleException(it.exception, customMessage = "Sign Up failed")
            }
        }
    }


    fun createOrUpdateProfile(name: String?= null,number: String?= null, imageUrl: String?= null ){

        var uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name?: userData.value?.name,
            number = number?: userData.value?.number,
            imageUrl = imageUrl ?: userData.value?.imageUrl
        )


        uid?.let {
            inProgress.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {

                if (it.exists()){
                    // update user data
                }else{
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProgress.value = false
                }
            }
                .addOnFailureListener(){
                    handleException(it,"Cannot Retrieve User")
                }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {

        Log.e("TAGit", "signup : Error -> ", exception)
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else customMessage
        eventMutableState.value = Events(message)
        inProgress.value = false


    }
}
