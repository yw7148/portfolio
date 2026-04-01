package com.youngwon.portfolio.project.infra.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "program")
open class ProgramJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int = 0

    @Column(name = "name", nullable = false)
    open lateinit var name: String

    @Column(name = "icon", nullable = false)
    open lateinit var icon: String

    protected constructor()
}
