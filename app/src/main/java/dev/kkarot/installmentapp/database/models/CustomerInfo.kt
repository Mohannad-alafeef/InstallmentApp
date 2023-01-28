package dev.kkarot.installmentapp.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.kkarot.installmentapp.database.converters.MyConverter
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "CustomerTable")
@TypeConverters(MyConverter::class)
data class CustomerInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customerId")
    var customerId:Long,
    @ColumnInfo(name = "customerName") var customerName:String,
    @ColumnInfo(name = "customerPhone") var customerPhone:String,
    @ColumnInfo(name = "customerAddress") var customerAddress:String,
    @ColumnInfo(name = "addDate")
    var addDate: Date
): Parcelable
