package br.edu.infnet.tasksapp.presentation.activities.reset_password

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.api.FirebaseAPI
import br.edu.infnet.tasksapp.data.DataStoreManager
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ResetPasswordViewModel() : ViewModel() {

    private val firebaseAPI = FirebaseAPI.instance

    fun resetPassword(email : String){
        val result = CompletableDeferred<Boolean>()
        viewModelScope.async(Dispatchers.IO){
            firebaseAPI.resetPassword(email).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    result.complete(true)
                } else {
                    result.complete(false)
                }
            }

            if(result.await()){
                onResetPasswordSuccess()
            }else{
                onResetPasswordFail()
            }
        }
    }

    var onResetPasswordSuccess: () -> Unit = {}
    var onResetPasswordFail: () -> Unit = {}

}