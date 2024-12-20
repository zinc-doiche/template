package com.devport.database.repository.template

import com.linecorp.kotlinjdsl.querymodel.jpql.JpqlQuery
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.render.jpql.JpqlRendered
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderer
import jakarta.persistence.EntityManager
import jakarta.persistence.Query

internal val jpqlRenderContext = JpqlRenderContext()

abstract class RepositoryTemplate(
    protected val entityManager: EntityManager
) {
    protected val jpqlRenderer = JpqlRenderer()

    fun JpqlQuery<*>.render(): JpqlRendered = jpqlRenderer.render(this, jpqlRenderContext)

    fun JpqlRendered.createQuery(): Query = entityManager.createQuery(query).apply {
        params.forEach(::setParameter)
    }
}