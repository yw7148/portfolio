package me.youngwon.portfolio.project.application

import me.youngwon.portfolio.project.ProgramView
import me.youngwon.portfolio.project.ProjectView

interface ProjectReader {
    fun findProjects(): List<ProjectView>

    fun findProgramsInProject(projectId: Int): List<ProgramView>
}
