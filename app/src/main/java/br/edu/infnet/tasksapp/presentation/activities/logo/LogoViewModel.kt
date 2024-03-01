package br.edu.infnet.tasksapp.presentation.activities.logo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.api.FirebaseAPI
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await

class LogoViewModel : ViewModel() {

    private val firebaseAPI = FirebaseAPI.instance

    suspend fun isLogged() : Deferred<Boolean> {
        val result = CompletableDeferred<Boolean>()
        viewModelScope.async(Dispatchers.IO) {
            try {
                val currentUser = firebaseAPI.currentUser()
                if (currentUser != null) {
                    onUserLogged()
                    result.complete(true)
                }else{
                    onError("Erro ao verificar o usuário")
                    result.complete(false)
                }
            } catch (e: Exception) {
                onError("Erro ao verificar o usuário")
                result.complete(false)
            }
            result.complete(false)
        }
        return result
    }
    var onUserLogged: () -> Unit = {}
    var onError: (String) -> Unit = {}
}