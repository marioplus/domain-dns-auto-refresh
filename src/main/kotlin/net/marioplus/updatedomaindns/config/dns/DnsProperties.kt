package net.marioplus.updatedomaindns.config.dns

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "dns")
data class DnsProperties(

    /**
     * 定时任务corn
     */
    val corn: String,
    /**
     * 账号信息
     */
    val accounts: List<DnsAccount>?,
)

