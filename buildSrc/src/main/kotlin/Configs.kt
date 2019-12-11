object Versions {
    const val kotlin = "1.3.50"
    const val ktor = "1.2.3"
    const val junit = "5.4.2"
    const val test_logger = "1.7.0"
    const val flyway = "6.0.0-beta"
    const val cfg4k = "0.9.0"
    const val hikaricp = "3.3.0"
    const val postgresqlJDBC = "42.2.5"
    const val jooq = "3.11.10"
    const val jooq_studer = "3.0.3"
    const val hibernate = "5.4.0.Final"
    const val gson = "2.8.5"
    const val google_guice = "4.2.2"
    const val slf4j_api = "1.7.25"
    const val valiktor_core = "0.5.0"
    const val logback_classic = "1.2.3"
    const val mindrot_jbcrypt = "0.4"
    const val jsoup = "1.11.3"
    const val rabbitmq_amqp_client = "5.6.0"
    const val ktlint_gradle = "9.1.1"
}

object Deps {
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val ktor_gson = "io.ktor:ktor-gson:${Versions.ktor}"
    const val ktor_locations = "io.ktor:ktor-locations:${Versions.ktor}"
    const val ktor_server_sessions = "io.ktor:ktor-server-sessions:${Versions.ktor}"
    const val ktor_server_test_host = "io.ktor:ktor-server-test-host:${Versions.ktor}"
    const val ktor_server_netty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    const val ktor_server_core = "io.ktor:ktor-server-core:${Versions.ktor}"
    const val ktor_client_cio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val ktor_client_gson = "io.ktor:ktor-client-gson:${Versions.ktor}"
    const val ktor_client_okhttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    const val ktor_client_websockets = "io.ktor:ktor-client-websockets:${Versions.ktor}"
    const val ktor_client_auth_jvm = "io.ktor:ktor-client-auth-jvm:${Versions.ktor}"
    const val ktor_auth_jwt = "io.ktor:ktor-auth-jwt:${Versions.ktor}"
    const val ktor_auth = "io.ktor:ktor-auth:${Versions.ktor}"

    const val mindrot_jbcrypt = "org.mindrot:jbcrypt:${Versions.mindrot_jbcrypt}"
    const val cfg4k_core = "com.jdiazcano.cfg4k:cfg4k-core:${Versions.cfg4k}"
    const val hikaricp = "com.zaxxer:HikariCP:${Versions.hikaricp}"

    const val postgresqlJDBC = "org.postgresql:postgresql:${Versions.postgresqlJDBC}"
    const val jooq_codegen = "org.jooq:jooq-codegen:${Versions.jooq}"
    const val jooq = "org.jooq:jooq:${Versions.jooq}"
    const val hibernate_core = "org.hibernate:hibernate-core:${Versions.hibernate}"
    const val flywaydb_flyway_core = "org.flywaydb:flyway-core:${Versions.flyway}"
    const val flywaydb_flyway_gradle_plugin = "org.flywaydb:flyway-gradle-plugin:${Versions.flyway}"
    const val google_guice = "com.google.inject:guice:${Versions.google_guice}"
    const val rabbitmq_amqp_client = "com.rabbitmq:amqp-client:${Versions.rabbitmq_amqp_client}"
    const val valiktor_core = "org.valiktor:valiktor-core:${Versions.valiktor_core}"
    const val logback_classic = "ch.qos.logback:logback-classic:${Versions.logback_classic}"


    const val slf4j_api = "org.slf4j:slf4j-api:${Versions.slf4j_api}"
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    const val jsoup= "org.jsoup:jsoup:${Versions.jsoup}"

    const val junit_juiter_api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
    const val junit_jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"

    const val ktlint_gradle = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlint_gradle}"

    const val exposed = ""
}

object Plugins {
    const val kotlin_noarg = "org.jetbrains.kotlin.plugin.noarg"
    const val kotlin_jpa = "org.jetbrains.kotlin.plugin.jpa"
    const val kotlin_allopen = "org.jetbrains.kotlin.plugin.allopen"
    const val test_logger = "com.adarshr.test-logger"
    const val flyway = "org.flywaydb.flyway"
    const val jooq_studer = "nu.studer.jooq"
    const val ktlint_gradle = "org.jlleitschuh.gradle.ktlint"
}

object Annotations {
    const val javax_entity = "javax.persistence.Entity"
    const val javax_mapped_super_class = "javax.persistence.MappedSuperclass"
    const val javax_embeddable = "javax.persistence.Embeddable"
}

object KotlinModules {
    const val jvm = "jvm"
    const val stdlib_jdk8 = "stdlib-jdk8"
}
