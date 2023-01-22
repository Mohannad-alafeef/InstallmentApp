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
@Entity(tableName = "InstallmentInfo")
@TypeConverters(MyConverter::class)
data class InstallmentInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "installment_id")
    val id:Long,
    @ColumnInfo(name = "customer_id")
    val customerId:Long,
    @ColumnInfo(name = "installment_title")
    val installmentTitle:String,
    @ColumnInfo(name = "original_price")
    val originalPrice:Float,
    @ColumnInfo(name = "installment_rate")
    val installmentRate:Int,
    @ColumnInfo(name = "profit")
    val profit:Float,
    @ColumnInfo(name = "total_price")
    val totalPrice:Float,
    @ColumnInfo(name = "start_date")
    val startDate:Date,
    @ColumnInfo(name = "end_Date")
    val endDate:Date,
    @ColumnInfo(name = "installment_type")
    val installmentType:String,
    @ColumnInfo(name = "payment")
    val payment:Float,
    @ColumnInfo(name = "reminder")
    val reminder:Float,
    @ColumnInfo(name = "payments")
    val payments:Payments

): Parcelable
