package org.cstemp.payment.model

import jakarta.persistence.*

/*
* @author Marcus Dashe
* */

@Entity
@Table(name="token")
class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(nullable = false, name = "token_key")
    var tokenKey : String = ""

    @Column(nullable = false, name = "payment_gateway")
    var gatewayProvider : String = ""

    @Column
    var mutable: Boolean = false
}