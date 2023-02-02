package org.cstemp.payment.controller

import org.cstemp.payment.service.SeerbitService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

/*
* @author Marcus Dashe
* */


@Controller
@RequestMapping("api/v1/")
class PaymentController (private val seerbitService: SeerbitService){

    @GetMapping("auth-token")
    fun getAuthToken(): ResponseEntity<String> = ResponseEntity.ok(this.seerbitService.auth())

    @GetMapping("banks")
    fun getBanks(): ResponseEntity<String> = ResponseEntity.ok(this.seerbitService.getBanks())

}