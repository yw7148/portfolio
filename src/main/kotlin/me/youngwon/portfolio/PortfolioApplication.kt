package com.youngwon.portfolio

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.modulith.Modulithic

@Modulithic
@SpringBootApplication
class PortfolioApplication

fun main(args: Array<String>) {
    runApplication<PortfolioApplication>(*args)
}
