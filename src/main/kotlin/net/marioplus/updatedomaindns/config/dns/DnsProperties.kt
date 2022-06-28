package net.marioplus.updatedomaindns.config.dns

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "dns")
data class DnsProperties(

    /**
     * 定时任务corn
     */
    val cron: String,
    /**
     * 账号信息
     */
    val accounts: List<DnsAccount>,
)

