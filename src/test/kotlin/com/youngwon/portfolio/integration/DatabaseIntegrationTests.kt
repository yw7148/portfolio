package com.youngwon.portfolio.integration

import com.youngwon.portfolio.home.entity.ContactEntity
import com.youngwon.portfolio.home.repository.ContactRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.MariaDBContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDateTime

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "spring.jwt.secret=test-secret",
        "spring.jpa.hibernate.ddl-auto=create",
    ],
)
class DatabaseIntegrationTests {
    @Autowired
    private lateinit var contactRepository: ContactRepository

    @Test
    fun `repositories work against MariaDB testcontainer`() {
        contactRepository.save(
            ContactEntity(
                id = 1,
                name = "Youngwon",
                email = "youngwon@example.com",
                message = "Hello",
                contactDate = LocalDateTime.now(),
            ),
        )

        assertEquals(1, contactRepository.findMaxId())
    }

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val mariadb = MariaDBContainer(DockerImageName.parse("mariadb:11.8"))
            .withDatabaseName("portfolio")
            .withUsername("test")
            .withPassword("test")
    }
}
