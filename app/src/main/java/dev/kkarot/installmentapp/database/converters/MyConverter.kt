package dev.kkarot.installmentapp.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import dev.kkarot.installmentapp.database.models.Payments
import java.util.*

class MyConverter {

    @TypeConverter
    fun toDate(timeStamp:Long): Date {
        return Date(timeStamp)
    }
    @TypeConverter
    fun toTimeStamp(date: Date):Long{
        return date.time
    }
    @TypeConverter
    fun fromClothingToJSON(payments: Payments): String {
        return Gson().toJson(payments)
    }
    @TypeConverter
    fun fromJSONToClothing(json: String): Payments {
        return Gson().fromJson(json,Payments::class.java)
    }
}