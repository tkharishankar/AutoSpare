package com.autospare.module

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Author: Hari K
 * Date: 30/11/2023.
 */
class Preferences(val dataStore: DataStore<Preferences>) {
    suspend inline fun <reified T> set(key: String, value: T) {
        val stringKey = stringPreferencesKey(key)
        val encodedValue = json.encodeToString(value)
        dataStore.edit { preferences ->
            preferences[stringKey] = encodedValue
        }
    }

    inline fun <reified T> get(key: String): Flow<T?> {
        val stringKey = stringPreferencesKey(key)
        return dataStore.data.map { preferences ->
            val jsonString = preferences[stringKey]
            try {
                jsonString?.let { json.decodeFromString<T>(it) }
            } catch (e: Exception) {
                println("Error while reading '$key': $e")
                null
            }
        }
    }

    val json = Json {
        classDiscriminator = "type"
        ignoreUnknownKeys = true
    }
}