package org.cstemp.payment.dto

/*
* @author Marcus Dashe
* */

object cs
enum class PaymentStatus {
    AWAITING,
    SUCCESS,
    FAILED,
}


enum class ServicePlatform {
    ELIMI,
    NSQ,
    SITEWORX,
    CAA,
    UNKNOWN
}

enum class OrganisationType {
    ACADEMIC,
    NGO,
    GOVERNMENT,
    PRIVATE,
}