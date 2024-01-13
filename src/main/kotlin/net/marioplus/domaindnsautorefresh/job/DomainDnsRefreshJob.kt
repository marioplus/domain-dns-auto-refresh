package net.marioplus.domaindnsautorefresh.job

import jakarta.annotation.Resource
import net.marioplus.domaindnsautorefresh.prop.ProjectProp
import net.marioplus.domaindnsautorefresh.remote.IpwIpOpenService
import net.marioplus.domaindnsautorefresh.utils.AliDnsUtils
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

@Component
class DomainDnsRefreshJob {

    @Resource
    private lateinit var ipOpenService: IpwIpOpenService

    @Resource
    private lateinit var projectProp: ProjectProp

    @Scheduled(cron = "\${net.marioplus.domain-dns-auto-refresh.cron}")
    fun doJob() {
        LOGGER.info("执行刷新dns解析记录定时任务,Counter: ${COUNTER.addAndGet(1L)}")
        projectProp.dnsAccounts.forEach { account ->
            account.records.forEach { record ->
                when (record.type) {
                    RECORD_TYPE_V4 -> ipOpenService.v4()
                        .apply {
                            LOGGER.info("ipv4: {}", this)
                        }

                    RECORD_TYPE_V6 -> ipOpenService.v6()
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
        private val LOGGER = LoggerFactory.getLogger(DomainDnsRefreshJob::class.java)
        private val COUNTER = AtomicLong()

        private val RECORD_TYPE_V4 = "A"

        private val RECORD_TYPE_V6 = "AAAA"
    }
}
