package net.marioplus.updatedomaindns.config.dns

import org.springframework.boot.context.properties.bind.DefaultValue

/**
 * dns 解析记录
 * @author marioplus
 */
data class DnsRecord(

    /**
     * 记录id
     */
    val recordId: String?,

    /**
     * 主机记录
     */
    val RR: String,

    /**
     * 域名
     */
    val domainName: String,

    /**
     * 记录类型
     */
    val type: String,

    /**
     * 解析缓存时间
     */
    @param:DefaultValue("600")
    val TTL: Long,

    /**
     * 记录值，一般是ip
     */
    val value: String?,

    )
