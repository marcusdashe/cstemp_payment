package org.cstemp.payment.controller
/*
* @author Marcus Dashe
* */

import org.cstemp.payment.dto.CardDTO
import org.cstemp.payment.service.SeerbitService
import org.cstemp.payment.service.TokenService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*


@CrossOrigin(origins = arrayOf("http://localhost:3000"))
@Controller
@RequestMapping("api/v1/payment")
class PaymentController (private val seerbitService: SeerbitService, private val tokenService: TokenService){

    @GetMapping("/auth-token")
    fun getAuthToken(): ResponseEntity<Any> =  ResponseEntity.ok().body(this.seerbitService.getSeerbitToken()) ?: ResponseEntity.notFound().build()

    @PostMapping("/pay-with-card")
    fun makeCardPayment(@RequestBody chargeBody: CardDTO): ResponseEntity<Any> = ResponseEntity.ok().body(this.seerbitService.chargeCard3ds(chargeBody, tokenService.client))

    @GetMapping("/banks")
    fun getBanks(): ResponseEntity<String> = ResponseEntity.ok().body(this.seerbitService.getBanks(tokenService.client))
}