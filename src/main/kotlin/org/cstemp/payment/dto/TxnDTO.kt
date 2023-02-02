package org.cstemp.payment.dto

/*
* @author Marcus Dashe
* */

data class TxnDTO(val currency: String,
                  val amount: Double,
                  val platform: ServicePlatform,
                  val status: PaymentStatus,
        )