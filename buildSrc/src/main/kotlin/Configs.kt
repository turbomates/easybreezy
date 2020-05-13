object Versions {
    const val kotlin = "1.3.72"
    const val ktor = "1.3.2"
    const val junit = "5.6.2"
    const val test_logger = "2.0.0"
    const val flyway = "6.4.1"
    const val cfg4k = "0.9.5"
    const val hikaricp = "3.4.5"
    const val postgresqlJDBC = "42.2.12"
    const val google_guice = "4.2.3"
    const val valiktor_core = "0.10.0"
    const val logback_classic = "1.2.3"
    const val mindrot_jbcrypt = "0.4"
    const val rabbitmq_amqp_client = "5.9.0"
    const val ktlint_gradle = "9.2.1"
    const val exposed = "0.24.1"
    const val kotlin_serialization = "1.3.72"
    const val kotlin_serialization_runtime = "0.20.0"
    const val ical4j = "3.0.14"
}

object Deps {
    const val kotlin_reflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    const val ktor_serialization = "io.ktor:ktor-serialization:${Versions.ktor}"
    const val ktor_locations = "io.ktor:ktor-locations:${Versions.ktor}"
    const val ktor_server_sessions = "io.ktor:ktor-server-sessions:${Versions.ktor}"
    const val ktor_server_test_host = "io.ktor:ktor-server-test-host:${Versions.ktor}"
    const val ktor_server_netty = "io.ktor:ktor-server-netty:${Versions.ktor}"
    const val ktor_server_core = "io.ktor:ktor-server-core:${Versions.ktor}"
    const val ktor_client_cio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val ktor_client_serialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val ktor_client_auth_jvm = "io.ktor:ktor-client-auth-jvm:${Versions.ktor}"
    const val ktor_auth_jwt = "io.ktor:ktor-auth-jwt:${Versions.ktor}"
    const val ktor_auth = "io.ktor:ktor-auth:${Versions.ktor}"

    const val mindrot_jbcrypt = "org.mindrot:jbcrypt:${Versions.mindrot_jbcrypt}"
    const val cfg4k_core = "com.jdiazcano.cfg4k:cfg4k-core:${Versions.cfg4k}"
    const val hikaricp = "com.zaxxer:HikariCP:${Versions.hikaricp}"

    const val postgresqlJDBC = "org.postgresql:postgresql:${Versions.postgresqlJDBC}"
    const val flywaydb_flyway_core = "org.flywaydb:flyway-core:${Versions.flyway}"
    const val google_guice = "com.google.inject:guice:${Versions.google_guice}"
    const val rabbitmq_amqp_client = "com.rabbitmq:amqp-client:${Versions.rabbitmq_amqp_client}"
    const val valiktor_core = "org.valiktor:valiktor-core:${Versions.valiktor_core}"
    const val logback_classic = "ch.qos.logback:logback-classic:${Versions.logback_classic}"

    const val junit_juiter_api = "org.junit.jupiter:junit-jupiter-api:${Versions.junit}"
    const val junit_jupiter_engine = "org.junit.jupiter:junit-jupiter-engine:${Versions.junit}"

    const val exposed_core = "org.jetbrains.exposed:exposed-core:${Versions.exposed}"
    const val exposed_dao = "org.jetbrains.exposed:exposed-dao:${Versions.exposed}"
    const val exposed_jdbc = "org.jetbrains.exposed:exposed-jdbc:${Versions.exposed}"
    const val kotlin_serialization = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin_serialization}"
    const val kotlin_serialization_runtime =
        "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.kotlin_serialization_runtime}"
    const val exposed_time = "org.jetbrains.exposed:exposed-java-time:${Versions.exposed}"
    const val ical4j = "org.mnode.ical4j:ical4j:${Versions.ical4j}"
}

object Plugins {
    const val test_logger = "com.adarshr.test-logger"
    const val flyway = "org.flywaydb.flyway"
    const val ktlint_gradle = "org.jlleitschuh.gradle.ktlint"
    const val kotlin_serialization = "org.jetbrains.kotlin.plugin.serialization"
}

object KotlinModules {
    const val jvm = "jvm"
    const val stdlib_jdk8 = "stdlib-jdk8"
}
