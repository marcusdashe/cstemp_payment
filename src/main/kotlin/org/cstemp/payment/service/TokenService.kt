package org.cstemp.payment.service

import com.google.gson.JsonObject
import com.seerbit.v2.Client
import com.seerbit.v2.Seerbit
import com.seerbit.v2.enums.EnvironmentEnum
import com.seerbit.v2.impl.SeerbitImpl
import com.seerbit.v2.service.AuthenticationService
import com.seerbit.v2.service.impl.AuthenticationServiceImpl
import org.cstemp.payment.exception.TokenNotFoundException
import org.cstemp.payment.model.Token
import org.cstemp.payment.repo.TokenRepo
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Service
class TokenService(private val tokenRepo: TokenRepo): CommandLineRunner {
    public lateinit var client: Client
    private lateinit var token: String

    @Value("\${seerbit.payment.gateway.publickey}")
    private val publicKey: String? = null

    @Value("\${seerbit.payment.gateway.secretkey}")
    private val privateKey: String? = null
    override fun run(vararg args: String?) {
        try{
            val seerbit: Seerbit = SeerbitImpl()
            client = Client()
            client.setApiBase(seerbit.getApiBase())
            client.setEnvironment(EnvironmentEnum.LIVE.getEnvironment())
            if(publicKey != null)  client.setPublicKey(publicKey)
            if(privateKey != null) client.setPrivateKey(privateKey)
            client.setTimeout(20)
            val authService: AuthenticationService = AuthenticationServiceImpl(client)
            val json: JsonObject = authService.doAuth()
            val jsonString = String.format("auth response: \n%s", json.toString())
            token = authService.token

            if(token == null || token.isEmpty()) {
                println("Token is either empty or null")
            } else {
                val tokenModel = Token()
                tokenModel.tokenKey = token
                tokenModel.gatewayProvider =  "Seerbit"
                val output = tokenRepo.save(tokenModel)
                println(token)
            }

        }catch(e: Exception){
            println(e.message)
            println("Error occured while getting or saving token")
        } finally {
            println("Control now handed to controllers")
        }
    }
}