package org.cstemp.payment.controller
/*
* @author Marcus Dashe
* */

import org.cstemp.payment.dto.ChargeDTO
import org.cstemp.payment.service.SeerbitService
import org.cstemp.payment.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping



@Controller
@RequestMapping("api/v1/payment")
class PaymentController (private val seerbitService: SeerbitService, private val tokenService: TokenService){

    @GetMapping("auth-token")
    fun getAuthToken(): ResponseEntity<Any> =  ResponseEntity.ok().body(this.seerbitService.getSeerbitToken()) ?: ResponseEntity.notFound().build()

    @PostMapping("/charge")
    fun charge(@RequestBody chargeBody: ChargeDTO): ResponseEntity<Any> = ResponseEntity.ok().body(this.seerbitService.chargeCard3ds(chargeBody, tokenService.client))

    @GetMapping("banks")
    fun getBanks(): ResponseEntity<String> = ResponseEntity.ok().body(this.seerbitService.getBanks(tokenService.client))
}