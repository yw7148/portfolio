package me.youngwon.portfolio.project.infra.persistence

import me.youngwon.portfolio.project.ProgramView
import me.youngwon.portfolio.project.ProjectView
import me.youngwon.portfolio.project.application.ProjectReader
import org.springframework.stereotype.Repository

@Repository
class ProjectPersistenceAdapter(
    private val projectJpaRepository: ProjectJpaRepository,
    private val projectProgramJpaRepository: ProjectProgramJpaRepository,
) : ProjectReader {
    override fun findProjects(): List<ProjectView> =
        projectJpaRepository.findAllByOrderByIdAsc().map { project ->
            ProjectView(
                id = project.id,
                name = project.name,
                category = project.category,
                img = project.img,
            )
        }

    override fun findProgramsInProject(projectId: Int): List<ProgramView> =
        projectProgramJpaRepository.findProgramViewsByProjectId(projectId)
}
