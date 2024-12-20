package com.devport.database

import com.devport.config.DataRootInfo
import jakarta.persistence.EntityManagerFactory
import com.devport.test.TestTemplate
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test

class EntityManagerFactoryProviderTest : TestTemplate() {
    private lateinit var entityManagerFactory: EntityManagerFactory

    @BeforeTest
    fun setUp() {
        entityManagerFactory = EntityManagerFactoryProvider.get(
            DataRootInfo(File(resource), "yml")
        )
    }

    @Test
    fun getTest() {
        assert(entityManagerFactory.isOpen)
    }
}