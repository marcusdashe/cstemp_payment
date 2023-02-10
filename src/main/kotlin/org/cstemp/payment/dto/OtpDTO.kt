package org.cstemp.payment.dto

data class OtpDTO(
        val otp: String,
        val linkingReference: String,
        val token: String
)
