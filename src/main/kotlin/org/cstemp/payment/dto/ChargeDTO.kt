package org.cstemp.payment.dto

/*
* @author Marcus Dashe
* */

data class ChargeDTO(
    var token: String,
    var amount: Double,
    var fee: Double,
    var fullName: String,
    var mobileNumber: String,
    var email: String,
    var currencyCode: String = "NGN",
    var countryCode: String = "NG",
    var paymentReference: String?,
    var productID: String?,
    var productDesc: String?,
    var clientAppCode: String?,
    var redirectURL: String,
    var channelType: String = "MasterCard",
    var deviceType: String?,
    var sourceIP: String?,
    var cardNumber: String,
    var cvv: String,
    var expiryMonth: Short,
    var expiryYear: Short,
    var pin: Int,
    var retry: Boolean = false,
    var paymentType: String = "CARD",
    var invoiceNumber: String,


)