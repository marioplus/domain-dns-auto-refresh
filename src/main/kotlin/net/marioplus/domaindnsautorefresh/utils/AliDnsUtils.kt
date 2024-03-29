package net.marioplus.domaindnsautorefresh.utils

import com.aliyun.alidns20150109.Client
import com.aliyun.alidns20150109.models.AddDomainRecordRequest
import com.aliyun.alidns20150109.models.DescribeDomainRecordsRequest
import com.aliyun.alidns20150109.models.DescribeDomainRecordsResponseBody
import com.aliyun.alidns20150109.models.UpdateDomainRecordRequest
import com.aliyun.teaopenapi.models.Config
import net.marioplus.domaindnsautorefresh.prop.DnsAccountProp
import net.marioplus.domaindnsautorefresh.prop.DnsRecordProp
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

class AliDnsUtils {
    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AliDnsUtils::class.java)

        private val CLIENTS = ConcurrentHashMap<String, Client>()

        /**
         * 构建 client
         */
        private fun initClient(dnsAccount: DnsAccountProp): Client {
            val config = Config()
                .setAccessKeyId(dnsAccount.accessKeyId)
                .setAccessKeySecret(dnsAccount.accessKeySecret)
                .setEndpoint(dnsAccount.endpoint)
            return Client(config)
        }

        /**
         * 获取 client
         */
        private fun getClient(dnsAccount: DnsAccountProp): Client {
            val key: String = "${dnsAccount.accessKeyId}:${dnsAccount.accessKeySecret}:${dnsAccount.endpoint}"
            val client = CLIENTS[key] ?: initClient(dnsAccount)
            CLIENTS[key] = client
            return client
        }

        /**
         * 创建域名解析记录
         */
        fun createDomainRecord(dnsAccount: DnsAccountProp, dnsRecordProp: DnsRecordProp, value: String): Boolean {
            AddDomainRecordRequest()
                .setDomainName(dnsRecordProp.domainName)
                .setRR(dnsRecordProp.RR)
                .setType(dnsRecordProp.type)
                .setValue(value)
                .setTTL(dnsRecordProp.TTL)
                .let {
                    try {
                        getClient(dnsAccount).addDomainRecord(it)
                        return true
                    } catch (e: Exception) {
                        LOGGER.error("创建dns解析记录失败", e)
                        return false
                    }
                }
        }

        /**
         * 查询nds记录值
         */
        fun queryDomainRecord(dnsAccount: DnsAccountProp, dnsRecordProp: DnsRecordProp)
                : DescribeDomainRecordsResponseBody.DescribeDomainRecordsResponseBodyDomainRecordsRecord? {
            val request = DescribeDomainRecordsRequest()
                .setDomainName(dnsRecordProp.domainName)
                .setRRKeyWord(dnsRecordProp.RR)
                .setType(dnsRecordProp.type)
                .setPageNumber(1)
                .setPageSize(10)

            return try {
                val response = getClient(dnsAccount)
                    .describeDomainRecords(request)
                // RR为模糊查询需要手动比较
                val records = response.body.domainRecords.record
                records.find { it.RR.equals(dnsRecordProp.RR) }
            } catch (e: Exception) {
                LOGGER.error("查询dns解析记录失败", e)
                LOGGER.error("dnsRecord: $dnsRecordProp")
                null
            }
        }

        /**
         * 更新nds记录值
         */
        fun updateDnsRecord(dnsAccount: DnsAccountProp, dnsRecordProp: DnsRecordProp, value: String): Boolean {

            val currRecord = queryDomainRecord(dnsAccount, dnsRecordProp)
                ?.also {
                    LOGGER.info(
                        "查询到dns记录：[recordId={}, RR={}, domainName={}, type={}, TTL={}, value={}]",
                        it.recordId,
                        it.RR,
                        it.domainName,
                        it.type,
                        it.TTL,
                        it.value
                    )
                }
            // 没有记录需要创建
                ?: return createDomainRecord(dnsAccount, dnsRecordProp, value)

            // 记录值相同
            if (currRecord.value.equals(value)) {
                LOGGER.info("当前记录无需更新，ip: $value")
                return false
            }

            // 更新
            UpdateDomainRecordRequest()
                .setRecordId(currRecord.recordId)
                .setRR(dnsRecordProp.RR)
                .setType(dnsRecordProp.type)
                .setTTL(dnsRecordProp.TTL)
                .setValue(value)
                .let {
                    try {
                        getClient(dnsAccount).updateDomainRecord(it)
                        LOGGER.info("更新dns记录成功：${dnsRecordProp.RR}.${dnsRecordProp.domainName}: ${currRecord.value} -> $value")
                        return true
                    } catch (e: Exception) {
                        LOGGER.error("更新dns记录失败, record: $dnsRecordProp", e)
                        return false
                    }
                }
        }
    }
}
