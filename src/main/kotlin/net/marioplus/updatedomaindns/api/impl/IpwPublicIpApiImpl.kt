package net.marioplus.updatedomaindns.api.impl

import jakarta.annotation.Resource
import net.marioplus.updatedomaindns.api.PublicIpApi
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class IpwPublicIpApiImpl : PublicIpApi {

    @Resource
    private lateinit var restTemplate: RestTemplate

    override fun getIpv4(): String? {
        return restTemplate.getForObject("http://4.ipw.cn", String::class.java)
    }

    override fun getIpv6(): String? {
        return restTemplate.getForObject("http://6.ipw.cn", String::class.java)
    }
}
