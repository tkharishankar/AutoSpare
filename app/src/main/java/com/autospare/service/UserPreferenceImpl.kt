package com.autospare.service

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.autospare.service.KEYS.KEY_USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Author: Hari K
 * Date: 23/11/2023.
 */
class UserPreferenceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : UserPreference {
    override fun userName(): Flow<String> {
        return dataStore.data
            .catch {
                it.stackTrace
                emit(emptyPreferences())
            }
            .map { preference ->
                preference[KEY_USER_ID] ?: ""
            }
    }

    override suspend fun saveUserName(name: String) {
        Log.i("Tag", "name: $name")
        dataStore.edit { preference ->
            preference[KEY_USER_ID] = name
        }
    }
}

object KEYS {
    val KEY_USER_ID = stringPreferencesKey("user_id")
}
