package io.easybreezy.hr.model.location

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import java.util.UUID

typealias LocationId = UUID

class Location private constructor(id: EntityID<LocationId>) : UUIDEntity(id) {
    private var name by Locations.name

    companion object : PrivateEntityClass<LocationId, Location>(object : Repository() {}) {
        fun create(name: String): Location {
            return Location.new { this.name = name }
        }
    }

    abstract class Repository : UUIDEntityClass<Location>(Locations, Location::class.java) {
        override fun createInstance(entityId: EntityID<LocationId>, row: ResultRow?): Location {
            return Location(entityId)
        }
    }
}

object Locations : UUIDTable("locations") {
    val name = text("name").uniqueIndex()
}
