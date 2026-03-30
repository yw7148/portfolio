package com.youngwon.portfolio.home.repository

import com.youngwon.portfolio.home.entity.ProjectEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectRepository : JpaRepository<ProjectEntity, Int>
