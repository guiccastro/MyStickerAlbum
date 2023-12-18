package com.devgc.mystickeralbum

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.devgc.mystickeralbum.model.LocaleOption
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.Locale

private object PreferencesKeys {
    const val DATA_STORE_TITLE = "basic-keys-preferences"
    val CURRENT_LANGUAGE = stringPreferencesKey("current-language")
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PreferencesKeys.DATA_STORE_TITLE)

suspend fun saveCurrentLanguage(context: Context, currentLanguage: Locale) {
    context.dataStore.edit { settings ->
        settings[PreferencesKeys.CURRENT_LANGUAGE] =
            currentLanguage.language + "|" + currentLanguage.country
    }
}

suspend fun getCurrentLanguage(context: Context): Locale? {
    return context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.CURRENT_LANGUAGE]?.let {
            val localeValue = it.split("|")
            val language = localeValue.getOrNull(0)
            val country = localeValue.getOrNull(1)
            if (language != null && country != null) {
                Locale(language, country)
            } else {
                LocaleOption.English.locale
            }
        }
    }.first()
}