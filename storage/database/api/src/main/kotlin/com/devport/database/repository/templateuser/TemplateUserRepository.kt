package com.devport.database.repository.templateuser

import com.devport.database.domain.templateuser.TemplateUser
import com.devport.database.repository.template.RepositoryTemplate
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.entity.Entity
import jakarta.persistence.EntityManager
import java.util.UUID

class TemplateUserRepository(
    entityManager: EntityManager
) : RepositoryTemplate(entityManager) {

    fun insertOne(templateUser: TemplateUser) = entityManager.persist(templateUser)

    fun findById(id: Long): TemplateUser? = entityManager.find(TemplateUser::class.java, id)
}