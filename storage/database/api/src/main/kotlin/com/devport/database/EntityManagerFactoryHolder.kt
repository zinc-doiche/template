package com.devport.database

import com.devport.config.DataRootInfo
import jakarta.persistence.EntityManagerFactory

object EntityManagerFactoryHolder {
    private lateinit var entityManagerFactory: EntityManagerFactory

    fun init(dataRootInfo: DataRootInfo) {
        entityManagerFactory = EntityManagerFactoryProvider.get(dataRootInfo)
    }

}