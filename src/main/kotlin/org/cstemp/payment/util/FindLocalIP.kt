package org.cstemp.payment.util

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface

object PaymentUtils {
    fun findLocalhostIP(): String? {
        return try {
            var sysIP: String?
            val osName = System.getProperty("os.name")
            if (osName.contains("Windows")) {
                sysIP = InetAddress.getLocalHost().hostAddress
            } else {
                sysIP = getSystemIP4Linux("eth0")
                if (sysIP == null) {
                    sysIP = getSystemIP4Linux("eth1")
                    if (sysIP == null) {
                        sysIP = getSystemIP4Linux("eth2")
                        if (sysIP == null) {
                            sysIP = getSystemIP4Linux("usb0")
                        }
                    }
                }
            }
            sysIP
        } catch (e: Exception) {
            System.err.println("System IP Exp : " + e.message)
            null
        }
    }

    //For Linux OS
    fun getSystemIP4Linux(name: String): String? {
        return try {
            var ip : String?=null
            val networkInterface = NetworkInterface.getByName(name)
            val inetAddress = networkInterface.inetAddresses
            var currentAddress = inetAddress.nextElement()
            while (inetAddress.hasMoreElements()) {
                currentAddress = inetAddress.nextElement()
                if (currentAddress is Inet4Address && !currentAddress.isLoopbackAddress()) {
                    ip = currentAddress.toString()
                    break
                }
            }
            if (ip != null) {
                if (ip.startsWith("/")) {
                    ip = ip.substring(1)
                }
            }
            ip
        } catch (e: Exception) {
            System.err.println("System Linux IP Exp : " + e.message)
            null
        }
    }
//    fun isValidPaymentToken(token: String): Boolean {
//        return try {
//            PaymentCodeUtils.isValidPaymentCode(paymentCode)
//    }
}