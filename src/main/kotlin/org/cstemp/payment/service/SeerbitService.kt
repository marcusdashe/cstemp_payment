package org.cstemp.payment.service

/*
* @author Marcus Dashe
* */


import com.google.gson.Gson
import com.seerbit.v2.Client
import com.seerbit.v2.Seerbit
import com.seerbit.v2.enums.AuthType
import com.seerbit.v2.enums.EnvironmentEnum
import com.seerbit.v2.impl.SeerbitImpl
import com.seerbit.v2.model.*
import com.seerbit.v2.service.AccountService
import com.seerbit.v2.service.AuthenticationService
import com.seerbit.v2.service.CardService
import com.seerbit.v2.service.ResourceService
import com.seerbit.v2.service.impl.AccountServiceImpl
import com.seerbit.v2.service.impl.AuthenticationServiceImpl
import com.seerbit.v2.service.impl.CardServiceImpl
import com.seerbit.v2.service.impl.ResourceServiceImpl
import org.cstemp.payment.dto.*
import org.cstemp.payment.model.Txn
import org.cstemp.payment.repo.TokenRepo
import org.cstemp.payment.repo.TxnRepo
import org.cstemp.payment.util.PaymentUtils
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
            else -> { "No token store on the database"}
        }
    }

    fun chargeCard3ds(payload: CardDTO, client: Client): Txn {
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
        txn.platform = when(payload.redirectURL){
            domainMap.getValue("e-limi"), domainMap.getValue("ilimi") -> ServicePlatform.ELIMI;
            domainMap.getValue("nsq") -> ServicePlatform.NSQ
            domainMap.getValue("siteworx") -> ServicePlatform.SITEWORX
            domainMap.getValue("caa") -> ServicePlatform.CAA
            else -> ServicePlatform.UNKNOWN
        }
        try{
            val response = cardService.doPaymentCharge3DS(cardPayment)
            txn.status = PaymentStatus.SUCCESS
            println(response.toString())
        } catch (e: Exception) {
            txn.status = PaymentStatus.FAILED
        }
        finally{
            txn.amount = payload.amount
            txnResponse = txnRepo.save(txn)
        }
           return txnResponse
        }

    fun doCardAuthorize(payload: CardDTO, client: Client): Txn {
        println("================== start card authorize ==================")
        val cardService: CardService = CardServiceImpl(client, token)
        val cardPayment = CardPayment.builder().publicKey(client.publicKey).amount(payload.amount.toString()).fee(payload.fee.toString())
            .fullName(payload.fullName).mobileNumber(payload.mobileNumber).currency(payload.currencyCode).country(payload.countryCode)
            .paymentReference(payload.paymentReference).email(payload.email).productId(payload.productID)
            .productDescription(payload.productDesc).clientAppCode(payload.clientAppCode).redirectUrl(payload.redirectURL)
            .channelType(payload.channelType).deviceType(payload.deviceType).sourceIP(PaymentUtils.findLocalhostIP()).cardNumber(payload.cardNumber)
            .cvv(payload.cvv).expiryMonth(payload.expiryMonth.toString()).expiryYear(payload.expiryMonth.toString()).pin(payload.pin.toString()).retry(payload.retry.toString()).invoiceNumber(payload.invoiceNumber).type("3DSECURE").paymentType(payload.paymentType).build()

            println("request: " + Gson().toJson(cardPayment))

        val txn: Txn = Txn()
        txn.platform = when(payload.redirectURL){
            domainMap.getValue("e-limi"), domainMap.getValue("ilimi") -> ServicePlatform.ELIMI;
            domainMap.getValue("nsq") -> ServicePlatform.NSQ
            domainMap.getValue("siteworx") -> ServicePlatform.SITEWORX
            domainMap.getValue("caa") -> ServicePlatform.CAA
            else -> ServicePlatform.UNKNOWN
        }
        txn.customerName = payload.fullName
        txn.paymentReference = payload.paymentReference
        txn.invoiceNumber = payload.invoiceNumber

        try{
            val response = cardService.doAuthorize(cardPayment)
            txn.status = PaymentStatus.SUCCESS
        } catch (e: Exception) {
            txn.status = PaymentStatus.FAILED
        } finally{
            txn.amount = payload.amount
            txnResponse = txnRepo.save(txn)
        }
        return txnResponse
    }

    fun doCardNon3dCharge(payload: CardDTO, publicKey: String, privateKey: String): Txn{
        val seerbit: Seerbit = SeerbitImpl()
       val client = Client()
        client.apiBase = seerbit.apiBase
        client.environment = EnvironmentEnum.LIVE.environment
        client.publicKey = publicKey
        client.privateKey = privateKey
        client.setTimeout(20)
        client.authenticationScheme = AuthType.BASIC.type
        val authService: AuthenticationService = AuthenticationServiceImpl(client)
        val token = authService.basicAuthorizationEncodedString
        val cardPayment =
            CardPayment.builder().publicKey(client.publicKey).amount(payload.amount.toString()).fullName(payload.fullName)
                .mobileNumber(payload.mobileNumber).currency(payload.currencyCode).country(payload.countryCode).paymentReference(payload.paymentReference)
                .email(payload.email).pin(payload.pin.toString()).cardNumber(payload.cardNumber).cvv(payload.cvv).expiryMonth(payload.expiryMonth.toString())
                .expiryYear(payload.expiryMonth.toString()).productId(payload.productID).productDescription(payload.productDesc).build()
        val cardService: CardService = CardServiceImpl(client, token)
        val txn: Txn = Txn()
        txn.platform = when(payload.redirectURL){
            domainMap.getValue("e-limi"), domainMap.getValue("ilimi") -> ServicePlatform.ELIMI;
            domainMap.getValue("nsq") -> ServicePlatform.NSQ
            domainMap.getValue("siteworx") -> ServicePlatform.SITEWORX
            domainMap.getValue("caa") -> ServicePlatform.CAA
            else -> ServicePlatform.UNKNOWN
        }
        txn.customerName = payload.fullName
        txn.paymentReference = payload.paymentReference
        txn.invoiceNumber = payload.invoiceNumber
        try{
            val response = cardService.doPaymentChargeNon3D(cardPayment)
            txn.status = PaymentStatus.SUCCESS
        } catch (e: Exception) {
            txn.status = PaymentStatus.FAILED
        } finally {
            txn.amount = payload.amount
            txnResponse = txnRepo.save(txn)
        }
        return txnResponse
    }

    fun doValidateOtpForCard(client: Client, token: String, linkingReference: String, otp: String): String? {
        val cardService: CardService = CardServiceImpl(client, token)
        val otpCard = OtpCard()
        val otpTransactionsDetails = OtpTransactionsDetails()
        otpTransactionsDetails.linkingreference = linkingReference ?: "F2372727771772882727"
        otpTransactionsDetails.otp = otp ?: "273736"
        otpCard.transaction = otpTransactionsDetails
        return cardService.doValidate(otpCard).asString
    }

    fun accountAuthorize(client: Client, payload: AccountDTO): Txn {
        val accountService: AccountService = AccountServiceImpl(client, payload.token)
        val account = Account()
        account.fullName = payload.fullName
        account.email = payload.email
        account.mobileNumber = payload.mobileNumber
        account.publicKey = client.publicKey
        account.channelType = payload.channelType
        account.deviceType = payload.deviceType
        account.sourceIP = PaymentUtils.findLocalhostIP()
        account.paymentReference = payload.paymentReference
        account.currency = payload.currencyCode
        account.productDescription = payload.productDesc
        account.productId = payload.productID
        account.country = payload.countryCode
        account.fee = payload.fee.toString()
        account.amount = payload.amount.toString()
        account.clientAppCode = payload.clientAppCode
        account.redirectUrl = payload.redirectURL
        account.accountName = payload.accountName
        account.accountNumber = payload.accountNumber
        account.bankCode = payload.bankCode
        account.bvn = payload.bvn
        account.retry = payload.retry.toString()
        account.invoiceNumber = payload.invoiceNumber
        account.dateOfBirth = payload.dateOfBirth.toString()
        account.paymentType = payload.paymentType

        val txn: Txn = Txn()
        txn.platform = when(payload.redirectURL){
            domainMap.getValue("e-limi"), domainMap.getValue("ilimi") -> ServicePlatform.ELIMI;
            domainMap.getValue("nsq") -> ServicePlatform.NSQ
            domainMap.getValue("siteworx") -> ServicePlatform.SITEWORX
            domainMap.getValue("caa") -> ServicePlatform.CAA
            else -> ServicePlatform.UNKNOWN
        }
        txn.customerName = payload.fullName
        txn.paymentReference = payload.paymentReference
        txn.invoiceNumber = payload.invoiceNumber
        try {
            val response = accountService.doAuthorize(account)
            txn.status = PaymentStatus.SUCCESS
        } catch (e: Exception) {
            txn.status = PaymentStatus.FAILED
            e.printStackTrace()
        } finally{
            txn.amount = payload.amount
            txnResponse = txnRepo.save(txn)
        }
        return txnResponse
    }

    fun doValidateOtpForAccount(client: Client, payload: OtpDTO): String? {
        val otp = Otp()
        val accountService: AccountService = AccountServiceImpl(client, payload.token)
        otp.linkingReference = payload.linkingReference ?: "2399293JSNBJBSFSDFSDS"
        otp.otp = payload.otp ?: "123456"
        return accountService.doValidate(otp).asString
    }
    fun getBanks(client: Client): String {
        val resourceService: ResourceService = ResourceServiceImpl(client, token)
        val response = resourceService.getBankList(client?.publicKey)
        return response.toString()
    }
}