package com.devport.database

import com.devport.config.DataRootInfo
import com.devport.config.asConfigSpec
import com.devport.resources.database.Config
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.persistence.Entity
import jakarta.persistence.EntityManagerFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import org.reflections.util.FilterBuilder
import java.util.Properties

object EntityManagerFactoryProvider {

    fun get(dataRootInfo: DataRootInfo): EntityManagerFactory {
        val reflections = Reflections(
            ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.devport"))
                .setScanners(SubTypesScanner(false), TypeAnnotationsScanner())
                .filterInputsBy(FilterBuilder().includePackage("com.devport"))
        )
        val configuration = Configuration()

        reflections.getTypesAnnotatedWith(Entity::class.java)
            .forEach(configuration::addAnnotatedClass)
        configuration.properties = getProperties(dataRootInfo)

        return configuration.buildSessionFactory(
            StandardServiceRegistryBuilder()
                .applySettings(configuration.properties)
                .build()
        )
    }

    private fun getProperties(dataRootInfo: DataRootInfo): Properties {
        val config = Config::class.asConfigSpec(dataRootInfo).load()
            ?: throw IllegalStateException("Config를 불러오는 데 실패했어요.")
        val database = config.database
        val dataSource = database.dataSource
        val hibernate = database.hibernate
        val hikari = database.hikari

        val hikariDataSource = HikariDataSource(
            HikariConfig().apply {
                driverClassName = dataSource.driverName
                jdbcUrl = dataSource.getURL()
                username = dataSource.username
                password = dataSource.password
                isAutoCommit = hibernate.isAutoCommit
                maximumPoolSize = hikari.maximumPoolSize
                minimumIdle = hikari.minimumIdle
                idleTimeout = hikari.idleTimeout.toLong()
                connectionTimeout = hikari.connectionTimeout.toLong()
                initializationFailTimeout = -1
            }
        )

        return Properties().apply {
            putAll(
                mapOf(
                    "jakarta.persistence.nonJtaDataSource" to hikariDataSource,
                    "hibernate.show_sql" to hibernate.showSQL,
                    "hibernate.hbm2ddl.auto" to hibernate.hbm2ddl,
                    "hibernate.cache.use_second_level_cache" to false,
                    "hibernate.globally_quoted_identifiers" to true,
                    "hibernate.cache.region.factory_class" to "org.hibernate.cache.jcache.internal.JCacheRegionFactory",
                    "hibernate.transaction.jta.platform" to "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform"
                )
            )
        }
        return Properties()
    }

    private fun Config.Database.DataSource.getURL(): String {
        return "jdbc:$sourceName://$host:$port/$database"
    }
}
