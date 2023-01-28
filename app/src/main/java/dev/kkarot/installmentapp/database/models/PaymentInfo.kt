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
@Entity(tableName = "PaymentTable")
@TypeConverters(MyConverter::class)
data class PaymentInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "paymentId")
    var paymentId:Long,
    @ColumnInfo(name = "installmentId")
    var installmentId:Long,
    @ColumnInfo(name = "value")
    var value:Float,
    @ColumnInfo(name = "paymentDate")
    var paymentDate:Date,
    @ColumnInfo(name = "isPaid")
    var isPaid:Boolean,
    @ColumnInfo(name = "isDue")
    var isDue:Boolean
):Parcelable
