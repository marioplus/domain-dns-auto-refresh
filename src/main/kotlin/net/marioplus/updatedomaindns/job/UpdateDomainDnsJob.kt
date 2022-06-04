package net.marioplus.updatedomaindns.job

import jakarta.annotation.Resource
import net.marioplus.updatedomaindns.api.PublicIpApi
import net.marioplus.updatedomaindns.config.dns.DnsProperties
import net.marioplus.updatedomaindns.utils.AliDnsUtils
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class UpdateDomainDnsJob {

    @Resource
    private lateinit var ipApi: PublicIpApi

    @Resource
    private lateinit var dnsProperties: DnsProperties

    @Scheduled(cron = "\${dns.cron}")
    fun doJob() {
        LOGGER.info("执行刷新dns解析记录定时任务,Counter: ${COUNTER.addAndGet(1L)}")
        dnsProperties.accounts?.forEach { account ->
            account.records.forEach { record ->
                when (record.type) {
                    "A" -> ipApi.getIpv4()
                        .apply {
                            LOGGER.info("ipv4: {}", this)
                        }
                    "AAAA" -> ipApi.getIpv6()
                        .apply {
                            LOGGER.info("ipv6: {}", this)
                        }
                    else -> {
                        LOGGER.warn("不支持此种类型的记录解析：type: ${record.type}")
                        null
                    }
                }
                    // 能获取到值就去尝试更新
                    ?.let {
                        AliDnsUtils.updateDnsRecord(account, record, it)
                    }
            }
        }
    }


    companion object {
        private val LOGGER = LoggerFactory.getLogger(UpdateDomainDnsJob::class.java)
        private val COUNTER = AtomicLong()
    }
}
