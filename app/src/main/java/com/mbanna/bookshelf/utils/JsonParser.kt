package com.mbanna.bookshelf.utils

import android.content.Context
import com.mbanna.bookshelf.model.BookItem
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

object JsonParser {

     fun readCountriesFromJson(context: Context): List<String> {
        val countriesList = ArrayList<String>()

        try {
            val inputStream: InputStream = context.assets.open("countries.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val jsonObject = JSONObject(jsonString)
            val countriesObject = jsonObject.getJSONObject("data")
            for (countryCode in countriesObject.keys()) {
                val countryName = countriesObject.getJSONObject(countryCode).getString("country")
                countriesList.add(countryName)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return countriesList
    }

    fun parseJsonToBookItems(context: Context, fileName: String): List<BookItem> {
        val bookItemList = mutableListOf<BookItem>()

        try {
            val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val alias = jsonObject.optString("alias")
                val hits = jsonObject.optInt("hits")
                val id = jsonObject.optString("id")
                val image = jsonObject.optString("image")
                val lastChapterDate = jsonObject.optInt("lastChapterDate")
                val title = jsonObject.optString("title")

                val bookItem = BookItem(alias, hits, id, image, lastChapterDate, title)
                bookItemList.add(bookItem)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bookItemList
    }
}