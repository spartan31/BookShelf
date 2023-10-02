package com.mbanna.bookshelf.utils

import android.content.Context
import android.content.SharedPreferences

object FavoriteManager {
    private const val PREFS_NAME = "MyFavorites"
    private const val KEY_FAVORITES = "favoriteIds"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    private fun saveFavoriteIds(context: Context, favoriteIds: MutableSet<String>) {
        val sharedPreferences = getSharedPreferences(context)
        sharedPreferences.edit().putStringSet(KEY_FAVORITES, favoriteIds).apply()
    }

    fun addFavorite(context: Context, itemId: String) {
        val favoriteIds = getFavoriteIds(context).toMutableSet()
        favoriteIds.add(itemId)
        saveFavoriteIds(context, favoriteIds)
    }

    fun removeFavorite(context: Context, itemId: String) {
        val favoriteIds = getFavoriteIds(context).toMutableSet()
        favoriteIds.remove(itemId)
        saveFavoriteIds(context, favoriteIds)
    }

    fun isFavorite(context: Context, itemId: String): Boolean {
        val favoriteIds = getFavoriteIds(context)
        return favoriteIds.contains(itemId)
    }

    private fun getFavoriteIds(context: Context): Set<String> {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getStringSet(KEY_FAVORITES, mutableSetOf()) ?: mutableSetOf()
    }

}

