package dev.kkarot.installmentapp.database.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Payments(val paymentList:List<PaymentInfo>):Parcelable