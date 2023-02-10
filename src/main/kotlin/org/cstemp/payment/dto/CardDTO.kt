package org.cstemp.payment.dto

import java.util.*

/*
* @author Marcus Dashe
* */

data class CardDTO(
    var token: String,
    var amount: Double,
    var fee: Double = 0.0,
    var fullName: String,
    var mobileNumber: String,
    var email: String,
    var currencyCode: String = "NGN",
    var countryCode: String = "NG",
    var paymentReference: String? = UUID.randomUUID().toString().take(11),
    var productID: String?,
    var productDesc: String = "Payment for a courese on E-limi LMS platform",
    var clientAppCode: String = "App 1",
    var redirectURL: String,
    var channelType: String = "MasterCard",
    var deviceType: String = "Web Server",
    var cardNumber: String,
    var cvv: String,
    var expiryMonth: Short,
    var expiryYear: Short,
    var pin: Int,
    var retry: Boolean = false,
    var paymentType: String = "CARD",
    var invoiceNumber: String = UUID.randomUUID().toString().take(6),
    )