package org.cstemp.payment.dto

import java.util.*

data class AccountDTO(
    var token: String,
    var amount: Double,
    var fee: Double = 0.0,
    var fullName: String,
    var mobileNumber: String,
    var email: String,
    var channelType: String = "BANK_ACCOUNT",
    var currencyCode: String = "NGN",
    var countryCode: String = "NG",
    var paymentReference: String = UUID.randomUUID().toString().take(11),
    var productID: String?,
    var productDesc: String = "Payment for a course on E-limi LMS platform",
    var clientAppCode: String = "App1",
    var redirectURL: String,
    var deviceType: String = "Web Server",
    var accountName: String,
    var accountNumber: String,
    var bankCode: String,
    var bvn: String,
    var retry: Boolean = false,
    var invoiceNumber: String = UUID.randomUUID().toString().take(6),
    var dateOfBirth: String,
    var paymentType: String = "ACCOUNT",
)