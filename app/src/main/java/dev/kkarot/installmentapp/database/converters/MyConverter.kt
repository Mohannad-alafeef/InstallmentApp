package dev.kkarot.installmentapp.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.List

class MyConverter {

    @TypeConverter
    fun toDate(timeStamp:Long): Date {
        return Date(timeStamp)
    }
    @TypeConverter
    fun toTimeStamp(date: Date):Long{
        return date.time
    }

}