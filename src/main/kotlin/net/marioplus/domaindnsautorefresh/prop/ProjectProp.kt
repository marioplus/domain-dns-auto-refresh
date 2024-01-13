package net.marioplus.domaindnsautorefresh.prop

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.DefaultValue

@ConfigurationProperties("net.marioplus.domain-dns-auto-refresh")
data class ProjectProp(

    /**
     * 定时任务 corn
     */
    @param:DefaultValue("0 * * * * ?")
    val cron: String,

    /**
     * dns 账号信息
     */
    val dnsAccounts: List<DnsAccountProp>
)
