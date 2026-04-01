package com.youngwon.portfolio.project.application

import com.youngwon.portfolio.project.ProgramView
import com.youngwon.portfolio.project.ProjectView

interface ProjectReader {
    fun findProjects(): List<ProjectView>

    fun findProgramsInProject(projectId: Int): List<ProgramView>
}
