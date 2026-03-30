package com.youngwon.portfolio.home.service

import com.youngwon.portfolio.home.dto.Contact
import com.youngwon.portfolio.home.entity.ProgramEntity
import com.youngwon.portfolio.home.entity.ProjectEntity
import com.youngwon.portfolio.home.entity.ProjectProgramEntity
import com.youngwon.portfolio.home.repository.ContactRepository
import com.youngwon.portfolio.home.repository.ProjectRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PortfolioServiceTests {
    @Mock
    private lateinit var projectRepository: ProjectRepository

    @Mock
    private lateinit var contactRepository: ContactRepository

    @InjectMocks
    private lateinit var service: PortfolioService

    @Test
    fun `projects maps entities to DTOs`() {
        val projectEntity = mock(ProjectEntity::class.java)
        `when`(projectEntity.id).thenReturn(1)
        `when`(projectEntity.name).thenReturn("Portfolio")
        `when`(projectEntity.category).thenReturn("backend")
        `when`(projectEntity.img).thenReturn("portfolio.png")
        `when`(projectRepository.findAll()).thenReturn(listOf(projectEntity))

        val result = service.projects()

        assertEquals(1, result.size)
        assertEquals(1, result[0].id)
        assertEquals("Portfolio", result[0].name)
        assertEquals("backend", result[0].category)
        assertEquals("portfolio.png", result[0].img)
    }

    @Test
    fun `programs in project returns empty list when Id is null`() {
        assertTrue(service.programsInProject(null).isEmpty())
    }

    @Test
    fun `programs in project maps nested program data`() {
        val projectEntity = mock(ProjectEntity::class.java)
        val projectProgramEntity = mock(ProjectProgramEntity::class.java)
        val programEntity = mock(ProgramEntity::class.java)

        `when`(programEntity.name).thenReturn("Spring Boot")
        `when`(programEntity.icon).thenReturn("spring.svg")
        `when`(projectProgramEntity.program).thenReturn(programEntity)
        `when`(projectProgramEntity.link).thenReturn("https://spring.io")
        `when`(projectEntity.programs).thenReturn(mutableListOf(projectProgramEntity))
        `when`(projectRepository.findById(1)).thenReturn(Optional.of(projectEntity))

        val result = service.programsInProject(1)

        assertEquals(1, result.size)
        assertEquals("Spring Boot", result[0].name)
        assertEquals("spring.svg", result[0].icon)
        assertEquals("https://spring.io", result[0].link)
    }

    @Test
    fun `contact returns false when contact is null`() {
        assertFalse(service.contactYoungwon(null))
    }

    @Test
    fun `contact persists contact entity`() {
        val contact = Contact(
            name = "Youngwon",
            email = "youngwon@example.com",
            message = "Hello",
        )
        `when`(contactRepository.findMaxId()).thenReturn(7)
        `when`(contactRepository.save(any())).thenAnswer { invocation -> invocation.arguments[0] }

        val result = service.contactYoungwon(contact)

        assertTrue(result)

        val captor = ArgumentCaptor.forClass(com.youngwon.portfolio.home.entity.ContactEntity::class.java)
        verify(contactRepository).save(captor.capture())

        val savedEntity = captor.value
        assertEquals(8, savedEntity.id)
        assertEquals("Youngwon", savedEntity.name)
        assertEquals("youngwon@example.com", savedEntity.email)
        assertEquals("Hello", savedEntity.message)
        assertTrue(savedEntity.contactDate.isBefore(LocalDateTime.now().plusSeconds(1)))
    }
}
