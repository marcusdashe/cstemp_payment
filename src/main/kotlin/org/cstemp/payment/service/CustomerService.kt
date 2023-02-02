package org.cstemp.payment.service

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.model.Customer
import org.cstemp.payment.repo.CustomerRepo
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomerService(private val customerrepo: CustomerRepo) {

    fun save(customer: Customer): Customer =  this.customerrepo.save(customer)

    fun findCustomerById(id: UUID): Customer = this.customerrepo.getReferenceById(id)

    fun findAllCustomers(): List<Customer> = this.customerrepo.findAll()

    fun findCustomerByName(customer: String): Customer? = this.customerrepo.findByCustomerName(customer)

    fun findCustomerByEmail(email: String): Customer? = this.customerrepo.findByEmail(email)

    fun findCustomerByPhone(phone: String): Customer? = this.customerrepo.findByPhone(phone)

//    fun findCustomerByOrganization(org: OrganisationType): List<Customer?> = this.customerrepo.findByOrgType(org)
//
//    fun findCustomerByTxnCount(count: Int): List<Customer?> = this.customerrepo.findByTxnCount(count)

    fun findCustomerByDateofCreation(date: Date): List<Customer?> = this.customerrepo.findByCreated(date)

    fun deleteCustomerByID(id: UUID) = this.customerrepo.deleteById(id)

    fun customerExists(id: UUID): Boolean = this.customerrepo.existsById(id)
}