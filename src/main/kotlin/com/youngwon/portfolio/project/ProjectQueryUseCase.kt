package com.youngwon.portfolio.project

interface ProjectQueryUseCase {
    fun listProjects(): List<ProjectView>

    fun listProgramsInProject(projectId: Int?): List<ProgramView>
}
