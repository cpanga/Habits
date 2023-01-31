package com.example.habits.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converters {
    @TypeConverter
    fun fromStringToBool(value: String?): List<Boolean> {
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromBoolList(list: List<Boolean?>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}