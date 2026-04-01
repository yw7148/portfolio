package com.youngwon.portfolio.architecture

import com.youngwon.portfolio.PortfolioApplication
import org.junit.jupiter.api.Test
import org.springframework.modulith.core.ApplicationModules

class ApplicationModulesTests {
    @Test
    fun `verifies application module structure`() {
        ApplicationModules.of(PortfolioApplication::class.java).verify()
    }
}
