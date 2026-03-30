package com.youngwon.portfolio.home.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "project_program")
open class ProjectProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = 0

    @ManyToOne
    @JoinColumn(name = "program_id")
    open lateinit var program: ProgramEntity

    @ManyToOne
    @JoinColumn(name = "project_id")
    open lateinit var project: ProjectEntity

    open lateinit var link: String

    protected constructor()
}
