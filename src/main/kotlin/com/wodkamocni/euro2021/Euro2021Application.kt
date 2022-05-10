package com.wodkamocni.euro2021

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories()
class Euro2021Application

fun main(args: Array<String>) {

    @Suppress("SpreadOperator")
    runApplication<Euro2021Application>(*args)
}
