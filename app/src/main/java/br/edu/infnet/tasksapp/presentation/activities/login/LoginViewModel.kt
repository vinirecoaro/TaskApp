package br.edu.infnet.tasksapp.presentation.activities.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.domain.model.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.*

class LoginViewModel(private val context : Context) : ViewModel() {

    private val firebaseAPI = FirebaseAPI.instance

    suspend fun login(email: String, password: String)=
        viewModelScope.async(Dispatchers.IO){
            val user = User("",email, password)
            var successLogin = CompletableDeferred<Boolean>()
            firebaseAPI.login(user)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        //firebaseAPI.updateReferences()
                        successLogin.complete(true)
                    } else {
                        successLogin.complete(false)
                        val message = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalid_email_password)
                            else -> context.getString(R.string.login_error)
                        }
                        onError(message)
                    }
                }

            if(successLogin.await()){
                val currentUser = firebaseAPI.currentUser()
                if(currentUser?.isEmailVerified == true){
                    onUserLogged()
                }else{
                    onUserNotVerified()
                }
            }
    }

    var onUserLogged: () -> Unit = {}
    var onUserNotVerified : () -> Unit = {}
    var onError: (String) -> Unit = {}

}