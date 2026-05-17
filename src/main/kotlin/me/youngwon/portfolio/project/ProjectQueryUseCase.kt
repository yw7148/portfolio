package me.youngwon.portfolio.project

import me.youngwon.portfolio.project.ProgramView

interface ProjectQueryUseCase {
    fun listProjects(): List<ProjectView>

    fun listProgramsInProject(projectId: Int?): List<ProgramView>
}
