package me.youngwon.portfolio.api

import me.youngwon.portfolio.generated.api.ProjectsApi
import me.youngwon.portfolio.generated.model.ProgramSummary
import me.youngwon.portfolio.generated.model.ProjectSummary
import me.youngwon.portfolio.project.ProjectQueryUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectsApiController(
    private val projectQueryUseCase: ProjectQueryUseCase,
) : ProjectsApi {
    override fun listProjects(): ResponseEntity<List<ProjectSummary>> {
        val projects = projectQueryUseCase.listProjects().map { project ->
            ProjectSummary(
                id = project.id,
                name = project.name,
                category = project.category,
                img = project.img,
            )
        }

        return ResponseEntity.ok(projects)
    }

    override fun listProjectPrograms(projectId: Int): ResponseEntity<List<ProgramSummary>> {
        val programs = projectQueryUseCase.listProgramsInProject(projectId).map { program ->
            ProgramSummary(
                name = program.name,
                icon = program.icon,
                link = program.link,
            )
        }

        return ResponseEntity.ok(programs)
    }
}
