package com.github.krystiankowalik.splitme.api.usersservice.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.hibernate5.HibernateTransactionManager
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import org.springframework.orm.jpa.JpaDialect
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource


@Configuration
@EnableTransactionManagement
open class DatabaseConfig(@Value("\${db.driver}") private val DB_DRIVER: String,
                          @Value("\${db.password}") private val DB_PASSWORD: String,
                          @Value("\${db.url}") private val DB_URL: String,
                          @Value("\${db.username}") private val DB_USERNAME: String,
                          @Value("\${hibernate.dialect}") private val HIBERNATE_DIALECT: String,
                          @Value("\${hibernate.show_sql}") private val HIBERNATE_SHOW_SQL: String,
                          @Value("\${hibernate.hbm2ddl.auto}") private val HIBERNATE_HBM2DDL_AUTO: String,
                          @Value("\${entitymanager.packagesToScan}") private val ENTITYMANAGER_PACKAGES_TO_SCAN: String) {

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(DB_DRIVER)
        dataSource.url = DB_URL
        dataSource.username = DB_USERNAME
        dataSource.password = DB_PASSWORD
        return dataSource
    }

    @Bean
    open fun dataSource2(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(DB_DRIVER)
        dataSource.url = DB_URL
        dataSource.username = DB_USERNAME
        dataSource.password = DB_PASSWORD
        return dataSource
    }

    @Bean
    open fun sessionFactory(): LocalSessionFactoryBean {
        val sessionFactoryBean = LocalSessionFactoryBean()
        sessionFactoryBean.setDataSource(dataSource())
        sessionFactoryBean.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN)
        /*val hibernateProperties = Properties()
        hibernateProperties["hibernate.dialect"] = HIBERNATE_DIALECT
        hibernateProperties["hibernate.show_sql"] = HIBERNATE_SHOW_SQL
        hibernateProperties["hibernate.hbm2ddl.auto"] = HIBERNATE_HBM2DDL_AUTO*/
        sessionFactoryBean.hibernateProperties = additionalProperties()
        return sessionFactoryBean
    }

    @Bean
    open fun transactionManager(): HibernateTransactionManager {
        val transactionManager = HibernateTransactionManager()
        transactionManager.sessionFactory = sessionFactory().getObject()
        return transactionManager
    }

    @Bean(name = ["jpaTransactionManager"])
    open fun notDefaultTransactionManager(@Qualifier("entityManagerFactory")
                                          jpaEntityManagerFactory: EntityManagerFactory): PlatformTransactionManager {
        val txManager = JpaTransactionManager()
        txManager.entityManagerFactory = jpaEntityManagerFactory
        return txManager
    }

    @Bean(name = ["entityManagerFactory"])
    open fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource2()
        em.setPackagesToScan(ENTITYMANAGER_PACKAGES_TO_SCAN)
        em.persistenceUnitName = "notDefaultDb"
        em.setJpaProperties(additionalProperties())

        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setGenerateDdl(false)
        em.jpaVendorAdapter = vendorAdapter
        return em
    }

    @Bean
    open fun additionalProperties(): Properties {
        val hibernateProperties = Properties()
        hibernateProperties["hibernate.dialect"] = HIBERNATE_DIALECT
        hibernateProperties["hibernate.show_sql"] = HIBERNATE_SHOW_SQL
        hibernateProperties["hibernate.hbm2ddl.auto"] = HIBERNATE_HBM2DDL_AUTO
        return hibernateProperties;
    }
}