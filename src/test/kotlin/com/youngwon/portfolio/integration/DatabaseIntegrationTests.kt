package com.youngwon.portfolio.integration

import com.youngwon.portfolio.contact.ContactSubmission
import com.youngwon.portfolio.contact.ContactUseCase
import com.youngwon.portfolio.contact.infra.persistence.ContactJpaRepository
import com.youngwon.portfolio.project.ProjectQueryUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.oracle.OracleContainer
import org.testcontainers.utility.DockerImageName
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(disabledWithoutDocker = true)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    properties = [
        "spring.jwt.secret=test-secret",
    ],
)
class DatabaseIntegrationTests {
    @Autowired
    private lateinit var contactUseCase: ContactUseCase

    @Autowired
    private lateinit var contactRepository: ContactJpaRepository

    @Autowired
    private lateinit var projectQueryUseCase: ProjectQueryUseCase

    @Autowired
    private lateinit var jdbcTemplate: JdbcTemplate

    @BeforeEach
    fun resetDatabase() {
        jdbcTemplate.update("DELETE FROM project_program")
        jdbcTemplate.update("DELETE FROM program")
        jdbcTemplate.update("DELETE FROM project")
        jdbcTemplate.update("DELETE FROM contact")
    }

    @Test
    fun `flyway migrates schema and contact module persists messages`() {
        assertTrue(
            contactUseCase.createContact(
                ContactSubmission(
                    name = "Youngwon",
                    email = "youngwon@example.com",
                    message = "Hello",
                ),
            ),
        )
        assertTrue(
            contactUseCase.createContact(
                ContactSubmission(
                    name = "Youngwon",
                    email = "youngwon@example.com",
                    message = "Hello again",
                ),
            ),
        )

        assertEquals(1, contactRepository.findMaxId())
    }

    @Test
    fun `project module reads logically related rows`() {
        jdbcTemplate.update(
            "INSERT INTO project (id, category, name, img) VALUES (?, ?, ?, ?)",
            1,
            "backend",
            "Portfolio",
            "portfolio.png",
        )
        jdbcTemplate.update(
            "INSERT INTO program (id, name, icon) VALUES (?, ?, ?)",
            1,
            "Spring Boot",
            "spring.svg",
        )
        jdbcTemplate.update(
            "INSERT INTO project_program (id, program_id, project_id, link) VALUES (?, ?, ?, ?)",
            1,
            1,
            1,
            "https://spring.io",
        )

        val projects = projectQueryUseCase.listProjects()
        assertEquals(1, projects.size)
        assertEquals("Portfolio", projects[0].name)
        assertEquals("backend", projects[0].category)
        assertEquals("portfolio.png", projects[0].img)

        val programs = projectQueryUseCase.listProgramsInProject(projects[0].id)
        assertEquals(1, programs.size)
        assertEquals("Spring Boot", programs[0].name)
        assertEquals("spring.svg", programs[0].icon)
        assertEquals("https://spring.io", programs[0].link)
    }

    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val oracle = OracleContainer(DockerImageName.parse("gvenzl/oracle-free:slim-faststart"))
    }
}
