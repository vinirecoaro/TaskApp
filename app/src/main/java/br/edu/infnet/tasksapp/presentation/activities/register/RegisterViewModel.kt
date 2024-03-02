package br.edu.infnet.tasksapp.presentation.activities.register

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.domain.model.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class RegisterViewModel(private val context : Context) : ViewModel() {

    private val firebaseAPI = FirebaseAPI.instance
    suspend fun createUser(name : String, email: String, password: String) {
        val user = User(name, email, password)
        try {
            viewModelScope.async(Dispatchers.IO){
                val task = firebaseAPI.createUser(user).await()
                if (task.user != null) {
                    firebaseAPI.addNewUserOnDatabase(user.name)
                    onUserCreated()
                } else {
                    val message = context.getString(R.string.create_account_error)
                    onError(message)
                }
            }
        }catch (exception : Exception){
            val message = when (exception) {
                is FirebaseAuthInvalidCredentialsException -> context.getString(R.string.invalid_email_password)
                is FirebaseAuthUserCollisionException -> context.getString(R.string.email_already_used)
                is FirebaseAuthWeakPasswordException -> context.getString(R.string.min_password_char_message)
                else -> context.getString(R.string.create_account_error)
            }
            onError(message)
        }
    }

    suspend fun sendEmailVerificarion(){
        viewModelScope.async(Dispatchers.IO){
            firebaseAPI.sendEmailVerification()
                ?.addOnCompleteListener{
                    onSendEmailSuccess()
                }
                ?.addOnFailureListener{
                    onSendEmailFailure()
                }
        }
    }

    var onUserCreated: () -> Unit = {}
    var onError: (String) -> Unit = {}

    var onSendEmailSuccess: () -> Unit = {}
    var onSendEmailFailure: () -> Unit = {}
}