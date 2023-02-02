package org.cstemp.payment.service

/*
* @author Marcus Dashe
* */


import com.google.gson.JsonObject
import com.seerbit.v2.Client
import com.seerbit.v2.Seerbit
import com.seerbit.v2.enums.EnvironmentEnum
import com.seerbit.v2.impl.SeerbitImpl
import com.seerbit.v2.service.AuthenticationService
import com.seerbit.v2.service.ResourceService
import com.seerbit.v2.service.impl.AuthenticationServiceImpl
import com.seerbit.v2.service.impl.ResourceServiceImpl
import org.cstemp.payment.exception.TokenNotFoundException
import org.cstemp.payment.model.Token
import org.cstemp.payment.repo.TokenRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


@Service
class SeerbitService(private val tokenRepo: TokenRepo) {
    private var client: Client? = null
    private var token: String? = null

    @Value("\${seerbit.payment.gateway.publickey}")
    private val publicKey: String? = null

    @Value("\${seerbit.payment.gateway.secretkey}")
    private val privateKey: String? = null


    fun auth(): String? {
        var seerbit: Seerbit = SeerbitImpl()
        client = Client()

        client?.setApiBase(seerbit.getApiBase())
        client?.setEnvironment(EnvironmentEnum.LIVE.getEnvironment())
        client?.setPublicKey(publicKey)
        client?.setPrivateKey(privateKey)
        client?.setTimeout(20);

        val authService: AuthenticationService = AuthenticationServiceImpl(client)
        val json: JsonObject = authService.doAuth()
        val jsonString = String.format("auth response: \n%s", json.toString())
        println(jsonString)
        token = authService.token

        if(token is String && token!!.isEmpty()){
            throw TokenNotFoundException("Empty token")
        }
        if(tokenRepo.existsByTokenKey(token!!))
            return token as String?

        val tokenObj = Token()
        tokenObj.tokenKey = token!!
        tokenObj.paymentGateway =  "Seerbit"
        var newToken = tokenRepo.save(tokenObj).tokenKey
        return newToken
    }

    fun getBanks(): String {
        val resourceService: ResourceService = ResourceServiceImpl(client, token)
        val response = resourceService.getBankList(client?.publicKey)
        return response.toString()
    }

}