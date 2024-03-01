package br.edu.infnet.tasksapp.presentation.activities.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.domain.model.User
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class RegisterViewModel : ViewModel() {

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
                    val message = "Ocorreu um erro ao criar a conta. Tente novamente mais tarde."
                    onError(message)
                }
            }
        }catch (exception : Exception){
            val message = when (exception) {
                is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha inválidos."
                is FirebaseAuthUserCollisionException -> "Este e-mail já está em uso."
                is FirebaseAuthWeakPasswordException -> "A senha deve ter pelo menos 6 caracteres."
                else -> "Ocorreu um erro ao criar a conta. Tente novamente mais tarde."
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