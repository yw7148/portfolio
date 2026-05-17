package me.youngwon.portfolio.project.application

import me.youngwon.portfolio.project.ProgramView
import me.youngwon.portfolio.project.ProjectQueryUseCase
import me.youngwon.portfolio.project.ProjectView
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectQueryService(
    private val projectReader: ProjectReader,
) : ProjectQueryUseCase {
    @Transactional(readOnly = true)
    override fun listProjects(): List<ProjectView> = projectReader.findProjects()

    @Transactional(readOnly = true)
    override fun listProgramsInProject(projectId: Int?): List<ProgramView> {
        if (projectId == null) {
            return emptyList()
        }

        return projectReader.findProgramsInProject(projectId)
    }
}
