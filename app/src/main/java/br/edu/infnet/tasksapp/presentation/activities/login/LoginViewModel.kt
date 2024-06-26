package br.edu.infnet.tasksapp.presentation.activities.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.R
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.data.DataStoreManager
import br.edu.infnet.tasksapp.domain.model.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import kotlinx.coroutines.*

class LoginViewModel(application : Application) : AndroidViewModel(application) {

    private val firebaseAPI = FirebaseAPI.instance
    private val dataStore = DataStoreManager.getInstance(application)

    suspend fun login(email: String, password: String)=
        viewModelScope.async(Dispatchers.IO){
            val user = User("",email, password)
            var successLogin = CompletableDeferred<Boolean>()
            firebaseAPI.login(user)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        successLogin.complete(true)
                    } else {
                        successLogin.complete(false)
                        val message = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> getApplication<Application>().resources.getString(R.string.invalid_email_password)
                            else -> getApplication<Application>().resources.getString(R.string.login_error)
                        }
                        onError(message)
                    }
                }

            if(successLogin.await()){
                val currentUser = firebaseAPI.currentUser()
                if(currentUser?.isEmailVerified == true){
                    onUserLogged(currentUser.uid)
                }else{
                    onUserNotVerified()
                }
            }
    }

    var onUserLogged: (String) -> Unit = {}
    var onUserNotVerified : () -> Unit = {}
    var onError: (String) -> Unit = {}

    fun setUserId(userId : String){
        viewModelScope.launch {
            dataStore.setUserId(userId)
        }
    }

}