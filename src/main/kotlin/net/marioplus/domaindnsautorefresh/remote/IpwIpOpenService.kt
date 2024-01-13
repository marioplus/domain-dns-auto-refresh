package net.marioplus.domaindnsautorefresh.remote

import jakarta.annotation.Resource
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class IpwIpOpenService {

    @Resource
    private lateinit var restClient: RestClient

    fun v4(): String? {
        return restClient.get()
            .uri("https://4.ipw.cn")
            .retrieve()
            .body<String>()
    }

    fun v6(): String? {
        return restClient.get()
            .uri("https://6.ipw.cn")
            .retrieve()
            .body<String>()
    }
}
