package net.marioplus.domaindnsautorefresh.prop

import net.marioplus.domaindnsautorefresh.prop.DnsAccountProp
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties

@ConfigurationProperties("net.marioplus.domain-dns-auto-refresh")
data class ProjectProp(

    /**
     * 定时任务 corn
     */
    val cron: String,

    /**
     * dns 账号信息
     */
    val dnsAccounts: List<DnsAccountProp>
)
