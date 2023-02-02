package org.cstemp.payment.repo

/*
* @author Marcus Dashe
* */

import org.cstemp.payment.model.Token
import org.springframework.data.jpa.repository.JpaRepository

interface TokenRepo : JpaRepository<Token, Long>{
    fun findByTokenKey(tokenKey: String): Token
    fun existsByTokenKey(tokenKey: String): Boolean
}