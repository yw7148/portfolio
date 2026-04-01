package com.youngwon.portfolio.project.infra.persistence

import com.youngwon.portfolio.project.ProgramView
import com.youngwon.portfolio.project.ProjectView
import com.youngwon.portfolio.project.application.ProjectReader
import org.springframework.stereotype.Repository

@Repository
class JpaProjectReader(
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
