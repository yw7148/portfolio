package com.youngwon.portfolio.api

import com.youngwon.portfolio.home.dto.Program
import com.youngwon.portfolio.home.dto.Project
import com.youngwon.portfolio.home.service.PortfolioService
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ProjectsApiController::class, ContactsApiController::class)
class PortfolioRestApiControllerTests {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var portfolioService: PortfolioService

    @Test
    fun `list projects returns json payload`() {
        `when`(portfolioService.projects()).thenReturn(
            listOf(
                Project(
                    id = 1,
                    name = "Portfolio",
                    category = "backend",
                    img = "portfolio.png",
                ),
            ),
        )

        mockMvc.perform(get("/api/v1/projects"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Portfolio"))
            .andExpect(jsonPath("$[0].category").value("backend"))
            .andExpect(jsonPath("$[0].img").value("portfolio.png"))
    }

    @Test
    fun `list project programs returns json payload`() {
        `when`(portfolioService.programsInProject(1)).thenReturn(
            listOf(
                Program(
                    name = "Spring Boot",
                    icon = "spring.svg",
                    link = "https://spring.io",
                ),
            ),
        )

        mockMvc.perform(get("/api/v1/projects/1/programs"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].name").value("Spring Boot"))
            .andExpect(jsonPath("$[0].icon").value("spring.svg"))
            .andExpect(jsonPath("$[0].link").value("https://spring.io"))
    }

    @Test
    fun `contact returns created response`() {
        `when`(portfolioService.contactYoungwon(any())).thenReturn(true)

        mockMvc.perform(
            post("/api/v1/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "name": "Youngwon",
                      "email": "youngwon@example.com",
                      "message": "Hello"
                    }
                    """.trimIndent(),
                ),
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.success").value(true))
    }

    @Test
    fun `legacy portfolio page route is not served`() {
        mockMvc.perform(get("/portfolio"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `legacy root route is not served`() {
        mockMvc.perform(get("/"))
            .andExpect(status().isNotFound)
    }
}
