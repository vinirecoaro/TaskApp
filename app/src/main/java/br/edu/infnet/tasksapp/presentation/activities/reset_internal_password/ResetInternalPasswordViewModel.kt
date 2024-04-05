package br.edu.infnet.tasksapp.presentation.activities.reset_internal_password

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.infnet.tasksapp.data.DataStoreManager
import kotlinx.coroutines.launch

class ResetInternalPasswordViewModel(context : Context) : ViewModel() {

    private val dataStore = DataStoreManager.getInstance(context)

    fun setUserInternalPassword(userInternalPassword : String){
        viewModelScope.launch {
            dataStore.setUserInternalPassword(userInternalPassword)
        }
    }

}