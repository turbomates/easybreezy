import com.adarshr.gradle.testlogger.theme.ThemeType
import org.flywaydb.gradle.task.FlywayInfoTask
import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties
import java.util.TimeZone

group = "io.easybreezy"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        maven(url = "https://plugins.gradle.org/m2/")
    }

    dependencies {
        classpath(Deps.postgresqlJDBC)
    }
}

sourceSets.create("migrations") {
    java.srcDir("src/migrations/kotlin")
}

repositories {
    jcenter()
    mavenCentral()
}

plugins {
    kotlin(KotlinModules.jvm).version(Versions.kotlin)
    id(Plugins.flyway).version(Versions.flyway)
    id(Plugins.ktlint_gradle).version(Versions.ktlint_gradle)
    id(Plugins.kotlin_serialization).version(Versions.kotlin_serialization)

    java
    application
    io.easybreezy.shadow
    id(Plugins.test_logger) version Versions.test_logger
}

dependencies {
    implementation(kotlin(KotlinModules.stdlib_jdk8))
    implementation(Deps.kotlin_reflect)
    implementation(Deps.ktor_server_netty)
    implementation(Deps.ktor_server_sessions)
    implementation(Deps.ktor_locations)
    implementation(Deps.ktor_serialization)
    implementation(Deps.ktor_server_core)
    implementation(Deps.ktor_client_cio)
    implementation(Deps.ktor_client_serialization)
    implementation(Deps.ktor_client_auth_jvm)
    implementation(Deps.ktor_auth_jwt)
    implementation(Deps.ktor_auth)
    implementation(Deps.cfg4k_core)
    implementation(Deps.hikaricp)
    implementation(Deps.valiktor_core)
    implementation(Deps.google_guice)
    implementation(Deps.rabbitmq_amqp_client)
    implementation(Deps.postgresqlJDBC)
    implementation(Deps.flywaydb_flyway_core)
    implementation(Deps.mindrot_jbcrypt)
    implementation(Deps.exposed_core)
    implementation(Deps.exposed_dao)
    implementation(Deps.exposed_jdbc)
    implementation(Deps.exposed_time)
    implementation(Deps.kotlin_serialization)
    implementation(Deps.kotlin_serialization_runtime)
    implementation(Deps.ical4j)

    runtimeOnly(Deps.logback_classic)

    testImplementation(Deps.ktor_server_test_host)
    testImplementation(Deps.junit_juiter_api)
    testImplementation("com.h2database:h2:1.4.200")
    testRuntimeOnly(Deps.junit_jupiter_engine)

    dependencies.add("migrationsImplementation", Deps.flywaydb_flyway_core)
    dependencies.add("migrationsImplementation", Deps.kotlin_reflect)
    dependencies.add("migrationsImplementation", kotlin(KotlinModules.stdlib_jdk8))
    implementation(kotlin("stdlib-jdk8"))
}

ktlint {
    version.set("0.35.0")
    debug.set(false)
    verbose.set(true)
    android.set(false)
    outputToConsole.set(true)
    outputColorName.set("RED")
    ignoreFailures.set(true)
    enableExperimentalRules.set(false)
    disabledRules.set(setOf("import-ordering", "no-wildcard-imports"))
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
    }

    filter {
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

application {
    mainClassName = "io.easybreezy.application.MainKt"
}

tasks.register("createDefaultUser", JavaExec::class) {
    group = "user"
    main = "io.easybreezy.user.cli.CreateDefaultUserCommand"
    classpath = sourceSets["main"].runtimeClasspath
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "12"
        freeCompilerArgs = listOf(
            "-Xuse-experimental=io.ktor.locations.KtorExperimentalLocationsAPI",
            "-Xuse-experimental=kotlinx.serialization.ImplicitReflectionSerializer",
            "-Xuse-experimental=io.ktor.util.KtorExperimentalAPI",
            "-Xuse-experimental=kotlin.ExperimentalStdlibApi"
        )
    }
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_12
}

//  ----------------  TEST ----------------  //

testlogger {
    theme = ThemeType.PLAIN
}

tasks.withType<Test> {
    useJUnitPlatform()

    dependsOn(":loadTestSystemConfiguration")

    systemProperty("easybreezy.test", "true")
    testLogging {
        events("PASSED", "STARTED", "FAILED", "SKIPPED")
        showStandardStreams = true
    }

    doFirst {
        System.getProperties().forEach { (k, v) ->
            if (k.toString().startsWith("easybreezy.")) {
                systemProperty(k.toString(), v.toString())
            }
        }
    }
}

//  ----------------  END TEST ----------------  //

//  ----------------  MIGRATIONS ----------------  //

val migrationsPath = "${project.projectDir}/src/migrations/kotlin/io/easybreezy/migrations"

listOf("", "Test").forEach { testOrNotTest ->
    val taskName = "migrations${testOrNotTest}Migrate"

    tasks.register(taskName) {
        group = "migrations"
        dependsOn("migrationsClasses", ":load${testOrNotTest}SystemConfiguration")

        doLast {
            val jdbcUrl = System.getProperties().getOrDefault("easybreezy.jdbc.url", null) as String?
            val jdbcUser = System.getProperties().getOrDefault("easybreezy.jdbc.user", null) as String?
            val jdbcPassword = System.getProperties().getOrDefault("easybreezy.jdbc.password", null) as String?

            logger.lifecycle("Migrating database $jdbcUrl")

            flyway {
                validateOnMigrate = false
                outOfOrder = true
                baselineOnMigrate = true
                locations = listOf("classpath:io/easybreezy/migrations").toTypedArray()
                url = jdbcUrl
                user = jdbcUser
                password = jdbcPassword
            }
            tasks.withType<FlywayInfoTask> { runTask() }
            tasks.withType<FlywayMigrateTask> { runTask() }
        }
    }
}

tasks.register("migrationsGenerate") {
    group = "migrations"

    doLast {
        val migrationName = properties["migname"]
            ?: throw IllegalArgumentException("You must specify `migname` argument")

        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val timestamp = dateFormat.format(Date())
        val fullMigrationName = "V${timestamp}__$migrationName"
        val fullMigrationPath = "$migrationsPath/$fullMigrationName.kt"

        val template = file("${project.projectDir}/src/migrations/resources/Migration.kt.template").readText()
        val migrationBody = template.replace("\$MIGRATION_NAME", fullMigrationName)
        file(fullMigrationPath).writeText(migrationBody)
        logger.lifecycle("Migration $fullMigrationName has been generated")
    }
}

//  ----------------  END MIGRATIONS ----------------  //

//  ----------------  CONFIGURATIONS ----------------  //

val globalEasybreezyProperties = System.getProperties()
    .entries
    .filter { (k, _) -> k.toString().startsWith("easybreezy.") }
    .map { (k, v) -> Pair(k.toString(), v.toString()) }

tasks.register("loadSystemConfiguration") {
    group = "configuration"

    doLast {
        val properties = Properties()
        val localPropertiesFile = file("$projectDir/src/main/resources/local.properties")

        if (localPropertiesFile.exists()) {
            logger.lifecycle("Loading ${localPropertiesFile.path}")
            properties.load(localPropertiesFile.readText().byteInputStream())
        }

        for ((k, v) in properties.entries) {
            val sKey = k.toString()

            if (sKey.startsWith("easybreezy.")) {
                if (System.getProperty(sKey) == null) {
                    System.setProperty(sKey, v.toString())
                }

                continue
            }

            System.setProperty(k.toString(), v.toString())
        }

        for ((k, v) in globalEasybreezyProperties) {
            System.setProperty(k, v)
        }
    }
}

tasks.register("loadTestSystemConfiguration") {
    group = "configuration"

    doLast {
        val properties = Properties()
        val localPropertiesFile = file("$projectDir/src/main/resources/local-test.properties")

        if (localPropertiesFile.exists()) {
            logger.lifecycle("Loading ${localPropertiesFile.path}")
            properties.load(localPropertiesFile.readText().byteInputStream())
        }

        for ((k, v) in properties.entries) {
            System.setProperty(k.toString(), v.toString())
        }

        for ((k, v) in globalEasybreezyProperties) {
            System.setProperty(k, v)
        }

        System.setProperty("easybreezy.test", "true")
    }
}

//  ----------------  END CONFIGURATIONS ----------------  //
