package net.marioplus.updatedomaindns.api

interface PublicIpApi {

    /**
     * 获取ipv4
     */
    fun getIpv4(): String?

    /**
     * 获取ipv6
     */
    fun getIpv6(): String?
}


