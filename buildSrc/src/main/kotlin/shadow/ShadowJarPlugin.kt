package shadow

import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.named

class ShadowJarPlugin: Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        applyShadowPlugin()
        configureShadowJarTask()
    }

    private fun Project.applyShadowPlugin() {
        apply<ShadowPlugin>()
    }

    private fun Project.configureShadowJarTask(): TaskProvider<ShadowJar> {
        return tasks.named<ShadowJar>("shadowJar") {
            archiveBaseName.set("easybreezy")
            archiveClassifier.set("")
            archiveVersion.set("")
            mergeServiceFiles()
            val sourceSets = project.properties["sourceSets"] as SourceSetContainer

            sourceSets.whenObjectAdded {
                if (this.name == "migrations") {
                    from(this.output)
                }
            }
        }
    }
}
