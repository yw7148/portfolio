package com.youngwon.portfolio.project.application

import com.youngwon.portfolio.project.ProgramView
import com.youngwon.portfolio.project.ProjectView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ProjectQueryServiceTests {
    @Mock
    private lateinit var projectReader: ProjectReader

    @InjectMocks
    private lateinit var service: ProjectQueryService

    @Test
    fun `list projects delegates to reader`() {
        val project = ProjectView(
            id = 1,
            name = "Portfolio",
            category = "backend",
            img = "portfolio.png",
        )
        `when`(projectReader.findProjects()).thenReturn(listOf(project))

        val result = service.listProjects()

        assertEquals(listOf(project), result)
    }

    @Test
    fun `list programs returns empty list when project id is null`() {
        assertTrue(service.listProgramsInProject(null).isEmpty())
    }

    @Test
    fun `list programs delegates to reader when project id is present`() {
        val program = ProgramView(
            name = "Spring Boot",
            icon = "spring.svg",
            link = "https://spring.io",
        )
        `when`(projectReader.findProgramsInProject(1)).thenReturn(listOf(program))

        val result = service.listProgramsInProject(1)

        assertEquals(listOf(program), result)
        verify(projectReader).findProgramsInProject(1)
    }
}
