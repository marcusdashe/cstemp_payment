package org.cstemp.payment.controller

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.dto.ResponseMessage
import org.cstemp.payment.dto.TxnDTO
import org.cstemp.payment.model.Txn
import org.cstemp.payment.service.TxnService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*


@Controller
@RequestMapping("api_v1/txn")
class TxnController(private val txnService: TxnService) {

    @PostMapping("create")
    fun createTxn(@RequestBody txnBody: TxnDTO): ResponseEntity<Txn> {
        val txn = Txn()
        txn.amount = txnBody.amount
        txn.platform = txnBody.platform

        return ResponseEntity.ok(this.txnService.save(txn))
    }

    @GetMapping("{id}")
    fun getTxnByID(@PathVariable("id") id: UUID): ResponseEntity<Txn>{
        return ResponseEntity.ok(this.txnService.findTransactionById(id))
    }

    @GetMapping("all")
    fun getAllTxns(): ResponseEntity<out Any> {
        val response = if(this.txnService.findAllTransactions().isNotEmpty())
                            ResponseEntity.ok(this.txnService.findAllTransactions())
                        else
                            ResponseEntity.status(404).body(ResponseMessage("Empty Transactions"))
        return response
    }

    @GetMapping("delete/{id}")
    fun deleteTxnByID(@PathVariable("id") id: UUID): ResponseEntity<Unit>{
        return when(this.txnService.txnExists(id)){
            true -> ResponseEntity.ok(this.txnService.deleteTransactionByID(id))
            false -> ResponseEntity.notFound().build()
        }
    }
}