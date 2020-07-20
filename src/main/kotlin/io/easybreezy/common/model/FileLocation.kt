package io.easybreezy.common.model

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable

class FileLocation private constructor() : Embeddable() {
    var path by FileLocationTable.path
        private set

    companion object : EmbeddableClass<FileLocation>(FileLocation::class) {
        override fun createInstance(): FileLocation {
            return FileLocation()
        }

        fun create(path: String): FileLocation {
            val location = FileLocation()
            location.path = path
            return location
        }
    }
}

object FileLocationTable : EmbeddableTable() {
    val path = varchar("path", 255)
}
