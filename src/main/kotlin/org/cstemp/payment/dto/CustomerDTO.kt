package org.cstemp.payment.dto

/*
* @author Marcus Dashe
* */

data class CustomerDTO(
    val customerName: String = "",
    val email: String = "",
    val phone: String = "",
    val orgType: OrganisationType = OrganisationType.PRIVATE,

)