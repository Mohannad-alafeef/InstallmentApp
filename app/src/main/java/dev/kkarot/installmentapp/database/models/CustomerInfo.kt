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
@Entity(tableName = "CustomerInfo")
@TypeConverters(MyConverter::class)
data class CustomerInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id")
    val customerId:Long,
    @ColumnInfo(name = "customer_name") val customerName:String,
    @ColumnInfo(name = "customer_phone") val customerPhone:String,
    @ColumnInfo(name = "customer_address") val customerAddress:String,
    @ColumnInfo(name = "add_date")
    val addDate: Date
): Parcelable
