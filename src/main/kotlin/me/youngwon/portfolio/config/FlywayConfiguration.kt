package com.youngwon.portfolio.config

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.MigrationVersion
import org.springframework.beans.factory.config.BeanFactoryPostProcessor
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component
import javax.sql.DataSource

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Flyway::class, DataSource::class)
@ConditionalOnProperty(prefix = "spring.flyway", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class FlywayConfiguration(
    private val dataSource: DataSource,
    private val environment: Environment,
) {
    @Bean(initMethod = "migrate")
    fun flyway(): Flyway {
        val locations = environment.getProperty("spring.flyway.locations", "classpath:db/migration")
            .split(",")
            .map(String::trim)
            .filter(String::isNotEmpty)
            .toTypedArray()

        return Flyway.configure()
            .dataSource(dataSource)
            .locations(*locations)
            .validateOnMigrate(environment.getProperty("spring.flyway.validate-on-migrate", Boolean::class.java, true))
            .baselineOnMigrate(environment.getProperty("spring.flyway.baseline-on-migrate", Boolean::class.java, false))
            .baselineVersion(MigrationVersion.fromVersion(environment.getProperty("spring.flyway.baseline-version", "0.0.1")))
            .load()
    }
}

@Component
@ConditionalOnProperty(prefix = "spring.flyway", name = ["enabled"], havingValue = "true", matchIfMissing = true)
class EntityManagerFactoryDependsOnFlywayPostProcessor : BeanFactoryPostProcessor {
    override fun postProcessBeanFactory(beanFactory: org.springframework.beans.factory.config.ConfigurableListableBeanFactory) {
        if (!beanFactory.containsBeanDefinition("entityManagerFactory")) {
            return
        }

        val entityManagerFactory = beanFactory.getBeanDefinition("entityManagerFactory")
        val dependsOn = entityManagerFactory.dependsOn?.toMutableList() ?: mutableListOf()

        if ("flyway" !in dependsOn) {
            dependsOn += "flyway"
            entityManagerFactory.setDependsOn(*dependsOn.toTypedArray())
        }
    }
}
