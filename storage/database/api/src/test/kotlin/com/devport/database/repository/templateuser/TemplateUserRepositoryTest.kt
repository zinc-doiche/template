package com.devport.database.repository.templateuser

import com.devport.config.DataRootInfo
import com.devport.database.EntityManagerFactoryProvider
import com.devport.database.domain.templateuser.TemplateUser
import com.devport.test.TestTemplate
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityManagerFactory
import java.io.File
import java.util.UUID
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class TemplateUserRepositoryTest : TestTemplate() {
    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private lateinit var templateUserRepository: TemplateUserRepository

    @BeforeTest
    fun setUp() {
        entityManagerFactory = EntityManagerFactoryProvider.get(DataRootInfo(File(resource), "yml"))
        entityManager = entityManagerFactory.createEntityManager()
        templateUserRepository = TemplateUserRepository(entityManager)
    }

    @Test
    fun insertAndFindTest() {
        val user = TemplateUser("username", UUID.randomUUID())

        with(entityManager.transaction) {
            begin()

            templateUserRepository.insertOne(user)

            commit()
            begin()

            val foundUser: TemplateUser? = templateUserRepository.findById(1)

            commit()

            assertNotNull(foundUser, "No user found")
            logger.info("Found user: $foundUser")
        }
    }
}

