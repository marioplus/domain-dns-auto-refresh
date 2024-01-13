package net.marioplus.domaindnsautorefresh.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient

@Configuration
class ProjectConfig {

    @Bean
    fun restClient(): RestClient {
        return RestClient.create()
    }

}
