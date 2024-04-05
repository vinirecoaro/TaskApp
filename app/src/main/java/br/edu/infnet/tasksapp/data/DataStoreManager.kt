package br.edu.infnet.tasksapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class DataStoreManager private constructor(context: Context) {
    private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name="USER_INFO")
    private val dataStore = context.dataStore

    companion object {
        val userIdKey = stringPreferencesKey("USER_ID")
        val userInternalPassword = stringPreferencesKey("USER_INTERNAL_PASSWORD")

        @Volatile
        private var instance: DataStoreManager? = null

        fun getInstance(context: Context): DataStoreManager {
            return instance ?: synchronized(this) {
                instance ?: DataStoreManager(context).also { instance = it }
            }
        }
    }

    suspend fun setUserId(userId : String){
        dataStore.edit { preferences ->
            preferences[userIdKey] = userId
        }
    }

    suspend fun setUserInternalPassword(newUserInternalPassword: String){
        dataStore.edit { preferences ->
            preferences[userInternalPassword] = newUserInternalPassword
        }
    }

     fun getUserId() : Flow<String> {
        return dataStore.data
            .catch {
                exception ->
                if(exception is IOException){
                    emit(emptyPreferences())
                }else{
                    throw exception
                }
            }
            .map { preferences ->
                val userId = preferences[userIdKey] ?: ""
                userId
            }
    }
}