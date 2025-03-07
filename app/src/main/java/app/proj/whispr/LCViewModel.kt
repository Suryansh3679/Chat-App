package app.proj.whispr

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import app.proj.whispr.data.Events
import app.proj.whispr.data.USER_NODE
import app.proj.whispr.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.events.Event
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
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

    init {
        val currentUser = auth.currentUser
        signIn.value = currentUser != null
        currentUser?.uid?.let {
            getUserData(it)
        }
    }


    fun signUp(context: Context, name: String, number: String, email: String, password: String) {
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please Fill All Fields")
            return
        }
        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT)
                .show()
            Log.e("TAGit", "signup : Password must be at least 6 characters")
            return
        }
        //If email has useless spaces
        val trimmedEmail = email.trim()

        inProgress.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {

            if (it.isEmpty){
                auth.createUserWithEmailAndPassword(trimmedEmail, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d("TAGit", "signup : User Logged In")

                        signIn.value = true
                        createOrUpdateProfile(name,number)

                    } else {
                        handleException(it.exception, customMessage = "Sign Up failed")
                    }
                }
            }else{
                handleException(customMessage = "Number already exists")
                inProgress.value = false
            }
        }

    }


    fun logIn(email: String,password: String){
        if (email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please Fill All Fields")
            return
        }else{
            inProgress.value = true
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                if(it.isSuccessful){

                    signIn.value = true
                    inProgress.value = false
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                }else{
                    handleException(it.exception,customMessage = "Login Failed")
                }
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

                    getUserData(uid)
                }
            }
                .addOnFailureListener(){
                    handleException(it,"Cannot Retrieve User")
                }
        }
    }

    //    db → Refers to an instance of Firebase Firestore (FirebaseFirestore.getInstance()).
    //    .collection(USER_NODE) → Accesses the Firestore collection named USER_NODE (a constant that likely stores the Firestore collection name, e.g., "users").
    //    .document(uid) → Accesses a specific document within the "users" collection, identified by the uid parameter.
    //    .addSnapshotListener { value, error -> ... } → Attaches a real-time listener to this document. Whenever the document changes, the lambda function gets called with:
    //    value: The latest snapshot of the document.
    //    error: Any error that occurs while fetching the document.
    private fun getUserData(uid :String) {
        inProgress.value = true
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->

            if (error != null){
                handleException(error, "Cannot Retrieve User")
            }

            if (value != null){
                var user = value.toObject<UserData>()
                userData.value = user
                inProgress.value = false
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
