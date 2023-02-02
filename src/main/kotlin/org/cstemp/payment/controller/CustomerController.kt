package org.cstemp.payment.controller

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.dto.CustomerDTO
import org.cstemp.payment.dto.ResponseMessage
import org.cstemp.payment.model.Customer
import org.cstemp.payment.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*


@Controller
@RequestMapping("api_v0.1/customer")
class CustomerController(private val customerService: CustomerService) {

    @PostMapping("create")
    fun createCustomer(@RequestBody customerBody: CustomerDTO): ResponseEntity<Customer>{
        val customer = Customer()
        customer.customerName = customerBody.customerName
        customer.email = customerBody.email
        customer.phone = customerBody.phone
        customer.orgType = customerBody.orgType

        return ResponseEntity.ok(this.customerService.save(customer))
    }

    @GetMapping("{id}")
    fun getCustomerByID(@PathVariable("id") id: UUID): ResponseEntity<Customer>{
        return ResponseEntity.ok(this.customerService.findCustomerById(id))
    }

    @GetMapping("all")
    fun getAllCustomers(): ResponseEntity<out Any> {
        val response = if(this.customerService.findAllCustomers().isNotEmpty())  ResponseEntity.ok(this.customerService.findAllCustomers()) else ResponseEntity.status(404).body(ResponseMessage("Empty Customer"))

        return response
    }

    @GetMapping("delete/{id}")
    fun deleteCustomerByID(@PathVariable("id") id: UUID): ResponseEntity<Unit> {
        var response = if(this.customerService.customerExists(id)) ResponseEntity.ok(this.customerService.deleteCustomerByID(id)) else ResponseEntity.notFound().build()
        return response
    }
}