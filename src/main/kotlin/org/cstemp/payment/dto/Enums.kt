package org.cstemp.payment.dto

/*
* @author Marcus Dashe
* */

object cs
enum class PaymentStatus {
    UNINIT,
    SUCCESS,
    FAILED,
}


enum class ServicePlatform {
    ELIMI,
    NSQ,
    CAA
}

enum class OrganisationType {
    ACADEMIC,
    NGO,
    GOVERNMENT,
    PRIVATE,
}