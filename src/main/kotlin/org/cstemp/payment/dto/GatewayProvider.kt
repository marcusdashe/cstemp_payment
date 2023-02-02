package org.cstemp.payment.dto
/*
* @author Marcus Dashe
* */

data class GatewayProvider(private var name : String, private var publicKey: String, private var privateKey: String )
