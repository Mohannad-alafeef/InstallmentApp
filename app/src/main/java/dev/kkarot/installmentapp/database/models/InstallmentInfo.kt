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
@Entity(tableName = "InstallmentTable")
@TypeConverters(MyConverter::class)
data class InstallmentInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "installmentId")
    var installmentId:Long,
    @ColumnInfo(name = "customerId")
    var customerId:Long,
    @ColumnInfo(name = "installmentTitle")
    var installmentTitle:String,
    @ColumnInfo(name = "originalPrice")
    var originalPrice:Float,
    @ColumnInfo(name = "profitRate")
    var profitRate:Int,
    @ColumnInfo(name = "profit")
    var profit:Float,
    @ColumnInfo(name = "totalPrice")
    var totalPrice:Float,
    @ColumnInfo(name = "startDate")
    var startDate:Date? = null ,
    @ColumnInfo(name = "endDate")
    var endDate:Date? = null,
    @ColumnInfo(name = "paymentType")
    var paymentType:String = "",
    @ColumnInfo(name = "period")
    var period:Int = 0,
    @ColumnInfo(name = "received")
    var received:Int = 0,
    @ColumnInfo(name = "totalReceived")
    var totalReceived:Int = 0,
    @ColumnInfo(name = "payment")
    var payment:Float = 0f,
    @ColumnInfo(name = "reminder")
    var reminder:Float = 0f,
    @ColumnInfo(name = "prePayment")
    var prePayment:Float = 0f

): Parcelable
