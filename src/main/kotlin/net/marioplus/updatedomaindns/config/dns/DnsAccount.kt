package net.marioplus.updatedomaindns.config.dns

/**
 * nds 账号
 *
 * @author marioplus
 */
data class DnsAccount(
    /**
     * ak
     */
    val accessKeyId: String,

    /**
     * sk
     */
    val accessKeySecret: String,

    /**
     * endpoint
     */
    val endpoint: String,

    /**
     * 解析记录
     */
    val records: List<DnsRecord>
)
