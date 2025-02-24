package app.proj.whispr

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LCViewModel @Inject constructor(
    val auth : FirebaseAuth


) : ViewModel() {



    fun signUp(context:Context,name: String,number: String,email: String,password: String){

        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            Log.e("TAGit", "signup : Password must be at least 6 characters")
            return
        }

        auth.createUserWithEmailAndPassword(email,password) .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("TAGit", "signup : User Logged In")
            } else {
                Log.e("TAGit", "signup : Error -> ${it.exception?.message}")
            }
        }
    }
}
