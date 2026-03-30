package com.youngwon.portfolio.home.service

import com.youngwon.portfolio.home.dto.Contact
import com.youngwon.portfolio.home.dto.Program
import com.youngwon.portfolio.home.dto.Project
import com.youngwon.portfolio.home.entity.ContactEntity
import com.youngwon.portfolio.home.entity.ProjectEntity
import com.youngwon.portfolio.home.repository.ContactRepository
import com.youngwon.portfolio.home.repository.ProjectRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class PortfolioService(
    private val projectRepository: ProjectRepository,
    private val contactRepository: ContactRepository,
) {
    fun projects(): List<Project> =
        projectRepository.findAll().map(::mapToDto)

    private fun mapToDto(project: ProjectEntity): Project =
        Project(
            id = project.id,
            name = project.name,
            category = project.category,
            img = project.img,
        )

    @Transactional
    fun programsInProject(projectId: Int?): List<Program> {
        if (projectId == null) {
            return emptyList()
        }

        val project = projectRepository.findById(projectId).orElseThrow()

        return project.programs.map { programProject ->
            Program(
                name = programProject.program.name,
                icon = programProject.program.icon,
                link = programProject.link,
            )
        }
    }

    fun contactYoungwon(contact: Contact?): Boolean {
        if (contact == null) {
            return false
        }

        val maxId = contactRepository.findMaxId()
        val entity = ContactEntity(
            if (maxId == null) 0 else maxId + 1,
            contact.name,
            contact.email,
            contact.message,
            LocalDateTime.now(ZoneId.of("Asia/Seoul")),
        )

        contactRepository.save(entity)
        return true
    }
}
