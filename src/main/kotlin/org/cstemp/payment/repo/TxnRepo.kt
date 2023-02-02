package org.cstemp.payment.repo

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.dto.PaymentStatus
import org.cstemp.payment.dto.ServicePlatform
import org.cstemp.payment.model.Customer
import org.cstemp.payment.model.Txn
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface TxnRepo : JpaRepository<Txn, UUID> {
    fun findByAmount(amount: Double): List<Txn?>
    fun findByPlatform(platform: ServicePlatform): List<Txn?>
    fun findByStatus(status: PaymentStatus): List<Txn?>
    fun findByCreated(created: Date): List<Txn?>
    fun findByCustomer(customer: Customer): List<Txn>
}