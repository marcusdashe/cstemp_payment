package org.cstemp.payment.repo

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface CustomerRepo : JpaRepository<Customer, UUID> {
    fun findByCustomerName(customer: String): Customer?
    fun findByEmail(email: String): Customer?
    fun findByPhone(phone: String): Customer?

//    @Query("SELECT o FROM customers o WHERE o.organisation_type = ?1")
//    fun findByOrgType(organization: OrganisationType): List<Customer?>
//
//    @Query("SELECT u FROM customers u WHERE u.txn_count = ?1")
//    fun findByTxnCount(count: Int): List<Customer?>

    fun findByCreated(date: Date): List<Customer?>

}