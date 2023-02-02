package org.cstemp.payment.model

import jakarta.persistence.*
import org.cstemp.payment.dto.OrganisationType
import java.util.*
/*
* @author Marcus Dashe
* */


@Entity
@Table(name="customers")
class Customer {
   /*   @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long = 0
    */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID()

    @Column(name="customer_full_name", nullable = false)
    var customerName: String = ""

    @Column(unique = true, nullable = false)
    var email: String = ""

    @Column(name = "customer_phone", unique = true)
    var phone: String = ""

    @Column(name = "organisation_type")
    @Enumerated(EnumType.STRING)
    var orgType: OrganisationType = OrganisationType.PRIVATE

    @Column(name = "txn_count")
    var txnCount: Int = 0

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date()
}