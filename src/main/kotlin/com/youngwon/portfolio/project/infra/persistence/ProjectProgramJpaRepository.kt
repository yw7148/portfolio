package com.youngwon.portfolio.project.infra.persistence

import com.youngwon.portfolio.project.ProgramView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ProjectProgramJpaRepository : JpaRepository<ProjectProgramJpaEntity, Int> {
    @Query(
        """
        select new com.youngwon.portfolio.project.ProgramView(program.name, program.icon, projectProgram.link)
        from ProjectProgramJpaEntity projectProgram
        join ProgramJpaEntity program on program.id = projectProgram.programId
        where projectProgram.projectId = :projectId
        order by projectProgram.id
        """,
    )
    fun findProgramViewsByProjectId(@Param("projectId") projectId: Int): List<ProgramView>
}
