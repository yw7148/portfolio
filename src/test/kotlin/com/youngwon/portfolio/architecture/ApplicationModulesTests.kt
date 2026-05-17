package me.youngwon.portfolio.architecture

import me.youngwon.portfolio.PortfolioApplication
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ApplicationModulesTests {
    @Test
    fun `verifies application module structure`() {
        ApplicationModules.of(PortfolioApplication::class.java).verify()
    }
}
