package io.easybreezy.infrastructure.postgresql

import io.easybreezy.project.model.issue.Priority
import org.postgresql.util.PGobject

class PGEnum<T : Enum<T>>(enumTypeName: String, enumValue: T?) : PGobject() {
    init {
        value = when (enumValue) {
            is Priority.Color -> enumValue.rgb
            else -> enumValue?.name
        }
        type = enumTypeName
    }
}
