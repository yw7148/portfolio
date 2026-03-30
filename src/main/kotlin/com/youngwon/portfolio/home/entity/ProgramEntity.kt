package com.youngwon.portfolio.home.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "program")
open class ProgramEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = 0

    open lateinit var name: String

    open lateinit var icon: String

    @OneToMany(mappedBy = "program")
    open var projects: MutableList<ProjectProgramEntity> = mutableListOf()

    protected constructor()
}
