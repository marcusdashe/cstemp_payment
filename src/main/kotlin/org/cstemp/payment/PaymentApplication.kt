package org.cstemp.payment

/*
* @author Marcus Dashe
* */

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class PaymentApplication

fun main(args: Array<String>) {
	runApplication<PaymentApplication>(*args){
		setBannerMode(Banner.Mode.OFF)
	}
}
