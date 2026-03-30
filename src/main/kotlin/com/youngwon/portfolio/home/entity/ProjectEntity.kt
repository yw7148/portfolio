package com.youngwon.portfolio.home.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "project")
open class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Int = 0

    open lateinit var category: String

    open lateinit var name: String

    open var img: String? = null

    @OneToMany(mappedBy = "project")
    open var programs: MutableList<ProjectProgramEntity> = mutableListOf()

    protected constructor()
}
