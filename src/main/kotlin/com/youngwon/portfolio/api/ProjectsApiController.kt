package com.youngwon.portfolio.api

import com.youngwon.portfolio.generated.api.ProjectsApi
import com.youngwon.portfolio.generated.model.ProgramSummary
import com.youngwon.portfolio.generated.model.ProjectSummary
import com.youngwon.portfolio.home.service.PortfolioService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ProjectsApiController(
    private val portfolioService: PortfolioService,
) : ProjectsApi {
    override fun listProjects(): ResponseEntity<List<ProjectSummary>> {
        val projects = portfolioService.projects().map { project ->
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
        val programs = portfolioService.programsInProject(projectId).map { program ->
            ProgramSummary(
                name = program.name,
                icon = program.icon,
                link = program.link,
            )
        }

        return ResponseEntity.ok(programs)
    }
}
