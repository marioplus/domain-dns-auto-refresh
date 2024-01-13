package net.marioplus.domaindnsautorefresh.prop

/**
 * dns 账号记录
 */
data class DnsAccountProp(
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
    val records: List<DnsRecordProp>
)
