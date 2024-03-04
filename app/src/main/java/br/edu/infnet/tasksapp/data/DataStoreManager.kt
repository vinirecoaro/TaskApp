package br.edu.infnet.tasksapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow


class DataStoreManager(context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="USER_INFO")
    private val dataStore = context.dataStore

    companion object {
        val userIdKey = stringPreferencesKey("USER_ID")
    }

    suspend fun setUserId(userId : String){
        dataStore.edit { preferences ->
            preferences[userIdKey] = userId
        }
    }

/*    suspend fun getUserId() : Flow<String> {

    }*/
}