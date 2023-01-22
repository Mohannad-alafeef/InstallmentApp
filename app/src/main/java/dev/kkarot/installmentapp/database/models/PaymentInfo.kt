package dev.kkarot.installmentapp.database.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class PaymentInfo(
    val paymentDate:Date,
    var isPaid:Boolean,
    var isDue:Boolean
):Parcelable
