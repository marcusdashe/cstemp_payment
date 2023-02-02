package org.cstemp.payment.service

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.dto.PaymentStatus
import org.cstemp.payment.dto.ServicePlatform
import org.cstemp.payment.model.Customer
import org.cstemp.payment.model.Txn
import org.cstemp.payment.repo.TxnRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class TxnService(private val txnRepo: TxnRepo) {

    fun save(txn: Txn): Txn = this.txnRepo.save(txn)

    fun findTransactionById(id: UUID): Txn = this.txnRepo.getReferenceById(id)

    fun findAllTransactions(): List<Txn> = this.txnRepo.findAll()

    fun findTransactionByAmount(amount: Double): List<Txn?> = this.txnRepo.findByAmount(amount)

    fun findTransactionByPlatform(platform: ServicePlatform): List<Txn?> = this.txnRepo.findByPlatform(platform)

    fun findTransactionByStatus(status: PaymentStatus): List<Txn?> = this.txnRepo.findByStatus(status)

    fun findTransactionByCreationDate(date: Date): List<Txn?> = this.txnRepo.findByCreated(date)

    fun findTransactionByCustomer(customer: Customer): List<Txn> = this.txnRepo.findByCustomer(customer)


    fun deleteTransactionByID(id: UUID) = this.txnRepo.deleteById(id)

    fun txnExists(id: UUID): Boolean = this.txnRepo.existsById(id)
}

