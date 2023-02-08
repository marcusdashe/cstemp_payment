package org.cstemp.payment.service

/*
* @author Marcus Dashe
* */


import com.google.gson.JsonObject
import com.seerbit.v2.Client
import com.seerbit.v2.Seerbit
import com.seerbit.v2.demo.cards.Card3dsDemo
import com.seerbit.v2.enums.EnvironmentEnum
import com.seerbit.v2.impl.SeerbitImpl
import com.seerbit.v2.model.CardPayment
import com.seerbit.v2.service.AuthenticationService
import com.seerbit.v2.service.CardService
import com.seerbit.v2.service.ResourceService
import com.seerbit.v2.service.impl.AuthenticationServiceImpl
import com.seerbit.v2.service.impl.CardServiceImpl
import com.seerbit.v2.service.impl.ResourceServiceImpl
import org.cstemp.payment.dto.ChargeDTO
import org.cstemp.payment.dto.PaymentStatus
import org.cstemp.payment.dto.ServicePlatform
import org.cstemp.payment.exception.TokenNotFoundException
import org.cstemp.payment.model.Token
import org.cstemp.payment.model.Txn
import org.cstemp.payment.repo.TokenRepo
import org.cstemp.payment.repo.TxnRepo
import org.cstemp.payment.util.PaymentUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class SeerbitService(private val tokenRepo: TokenRepo, private val txnRepo: TxnRepo) {

    private lateinit var token: String
    private lateinit var txnResponse: Txn
    private val domainMap : Map<String, String> = mapOf(
        "e-limi" to "https://www.e-limi.africa",
        "ilimi" to "https://www.ilimi.africa",
        "nsq" to "https://nsq.e-limi.africa",
        "siteworx" to "https://siteworx.ng",
        "caa" to "http://caa.cstemp.org")
    fun getSeerbitToken(): String? {
        return when  {
            tokenRepo.existsByGatewayProvider("Seerbit") -> {
                token = tokenRepo.findByGatewayProvider("Seerbit").tokenKey
                token
            };
            tokenRepo.existsByGatewayProvider("Moniepoint") -> {
                token = tokenRepo.findByGatewayProvider("Moniepoint").tokenKey
                token
            }
            else -> { "No Token store on the database"}
        }
    }

    fun chargeCard3ds(payload: ChargeDTO, client: Client): Txn {
        println("================== start card 3ds charge ==================")
        val cardPayment = CardPayment.builder().publicKey(client?.getPublicKey()).amount(payload.amount.toString()).fee(payload.fee.toString())
            .fullName(payload.fullName).mobileNumber(payload.mobileNumber).currency(payload.currencyCode).country(payload.countryCode)
            .paymentReference(payload.paymentReference).email(payload.email).productId(payload.productID)
            .productDescription(payload.productDesc).clientAppCode(payload.clientAppCode).redirectUrl(payload.redirectURL)
            .channelType(payload.channelType).deviceType(payload.deviceType).sourceIP(PaymentUtils.findLocalhostIP())
            .cardNumber(payload.cardNumber).cvv(payload.cvv).expiryMonth(payload.expiryMonth.toString())
            .expiryYear(payload.expiryYear.toString()).pin(payload.pin.toString()).retry(payload.retry.toString())
            .paymentType(payload.paymentType).invoiceNumber(payload.invoiceNumber).build()

        val cardService: CardService = CardServiceImpl(client, payload.token)
        val txn = Txn()
        try{
            val response = cardService.doPaymentCharge3DS(cardPayment)
            txn.amount = payload.amount
            txn.platform = when(payload.redirectURL){
                domainMap.getValue("e-limi"), domainMap.getValue("ilimi") -> ServicePlatform.ELIMI;
                domainMap.getValue("nsq") -> ServicePlatform.NSQ
                domainMap.getValue("siteworx") -> ServicePlatform.SITEWORX
                domainMap.getValue("caa") -> ServicePlatform.CAA
                else -> ServicePlatform.UNKNOWN
            }
            txn.status = PaymentStatus.SUCCESS
            println(response.toString())
        } catch (e: Exception) {
            txn.platform = when(payload.redirectURL){
                domainMap.getValue("e-limi"), domainMap.getValue("ilimi") -> ServicePlatform.ELIMI;
                domainMap.getValue("nsq") -> ServicePlatform.NSQ
                domainMap.getValue("siteworx") -> ServicePlatform.SITEWORX
                domainMap.getValue("caa") -> ServicePlatform.CAA
                else -> ServicePlatform.UNKNOWN
            }
            txn.status = PaymentStatus.FAILED
        }
        finally{
           txnResponse = txnRepo.save(txn)
        }
        return txnResponse
        }


//    @JvmStatic
//    fun main(args: Array<String>) {
//        val token = Card3dsDemo.doAuthenticate()
//        val response = doCard3dsCharge(token)
//        println("card 3ds response: $response")
//    }

    fun getBanks(client: Client): String {
        val resourceService: ResourceService = ResourceServiceImpl(client, token)
        val response = resourceService.getBankList(client?.publicKey)
        return response.toString()
    }
}