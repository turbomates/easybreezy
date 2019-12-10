package io.easybreezy.application.db.test

import io.easybreezy.application.db.hibernate.DataSourceProvider
import io.easybreezy.application.db.hibernate.PostgreSQLCustomDialect
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.cfgxml.internal.ConfigLoader
import org.hibernate.boot.cfgxml.spi.LoadedConfig
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService
import javax.sql.DataSource

internal val lookupHibernate: (dataSource: DataSource) -> SessionFactory = {

    val provider = DataSourceProvider(it)
    val loadedConfig = LoadedConfig.baseline()
    val serviceRegistry = BootstrapServiceRegistryBuilder().enableAutoClose().build()
    val configLoader = ConfigLoader(serviceRegistry)
    val hibernateResources = serviceRegistry.getService(ClassLoaderService::class.java).locateResources("hibernate.cfg.xml")

    for (resourceURL in hibernateResources) {
        loadedConfig.merge(configLoader.loadConfigXmlUrl(resourceURL))
    }

    val registry = StandardServiceRegistryBuilder(serviceRegistry, loadedConfig)
        .applySetting("hibernate.connection.provider_class", provider)
        .applySetting("hibernate.current_session_context_class", "thread")
        .applySetting("org.hibernate.flushMode ", "MANUAL")
        .applySetting("hibernate.dialect", PostgreSQLCustomDialect::class.java.name)
        .applySetting("show_sql", true)
        .build()
    try {

        MetadataSources(registry).buildMetadata().buildSessionFactory()
    } catch (e: Exception) {
        // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
        // so destroy it manually.
        StandardServiceRegistryBuilder.destroy(registry)

        throw ExceptionInInitializerError("Initial SessionFactory failed$e")
    }
}

class TestHibernate(dataSource: DataSource) : SessionFactory by lookupHibernate(dataSource)
