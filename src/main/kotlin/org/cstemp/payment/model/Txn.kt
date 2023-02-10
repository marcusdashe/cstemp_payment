package org.cstemp.payment.model
/*
* @author Marcus Dashe
* */

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.cstemp.payment.dto.PaymentStatus
import org.cstemp.payment.dto.ServicePlatform
import org.hibernate.annotations.OnDelete
import org.hibernate.annotations.OnDeleteAction
import java.util.*



@Entity
@Table(name="transactions")
class Txn {
    /*@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
    */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID()

    @Column(name="customer_name")
    var customerName: String? = null

    @Column(name = "payment_reference", unique = true)
    var paymentReference: String? = null

    @Column(name = "invoice_number", unique = true)
    var invoiceNumber: String? = null

    @Column
    var currency: String = Currency.getInstance("NGN").symbol

    @Column
    var amount : Double = 0.0

    @Column
    @Enumerated(EnumType.STRING)
    var platform: ServicePlatform = ServicePlatform.ELIMI

    @Column(name = "txn_status")
    @Enumerated(EnumType.STRING)
    var status : PaymentStatus = PaymentStatus.AWAITING

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date = Date()

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    var modified: Date = Date()

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private lateinit var customer: Customer

    @PreUpdate
    protected  fun onUpdate(){
        this.modified = Date()
    }

}