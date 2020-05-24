package io.easybreezy.project.application.issue.command.language

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.io.File
import java.nio.file.Paths

class LanguageTranslator {

    fun load(): List<Translations> {
        val translations = mutableListOf<Translations>()
        val directory = Paths.get("", "src/main/resources/issue-parser").toAbsolutePath().toString()

        File(directory).walk().forEach { file ->
            if (file.isFile) {
                val bufferedReader = file.bufferedReader()
                translations.add(
                    Json.parse(bufferedReader.use { file.readText() })
                )
            }
        }

        return translations.toList()
    }
}

@Serializable
data class Translations(val priority: Map<String, String>)
