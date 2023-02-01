package dev.kkarot.installmentapp.cons

enum class InstallmentType {
    Annually,
    Monthly,
    Weekly
}

enum class PaymentOpt{
    Fraction,
    FirstPayment,
    LastPayment
}
enum class DeductType{
    FromTotal,
    FromPayments
}