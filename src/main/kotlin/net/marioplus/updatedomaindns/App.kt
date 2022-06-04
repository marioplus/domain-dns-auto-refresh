package net.marioplus.updatedomaindns

import net.marioplus.updatedomaindns.config.dns.DnsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@EnableConfigurationProperties(value = [DnsProperties::class])
class App

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
