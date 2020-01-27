package io.easybreezy.infrastructure.exposed

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.Embedded
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame

object Users : IntIdTable() {
    val age = integer("age").nullable()
    val avatar = varchar("info_avatar", 25)
    val firstName = varchar("first_name", 25)
    val lastName = varchar("last_name", 25)
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var age by Users.age
    var info by Embedded(Info)

    class Info private constructor() : Embeddable() {
        var name by Embedded(Name)
        var avatar by Users.avatar

        companion object : EmbeddableClass<Info>(Info::class) {
            operator fun invoke(avatar: String, name: Name): Info {
                val info = Info()
                info.avatar = avatar
                info.name = name
                return info
            }

            override fun createInstance(resultRow: ResultRow?): Info {
                return Info()
            }
        }
    }

    class Name private constructor() : Embeddable() {
        var firstName by Users.firstName
        var lastName by Users.lastName

        companion object : EmbeddableClass<Name>(Name::class) {
            operator fun invoke(firstName: String, lastName: String): Name {
                val name = Name()
                name.firstName = firstName
                name.lastName = lastName
                return name
            }

            override fun createInstance(resultRow: ResultRow?): Name {
                return Name()
            }
        }
    }
}

@Test
fun main() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")

    transaction {
        SchemaUtils.create(Users)
        val name = User.Name("first", "last")
        val nextName = User.Name("firstNext", "lastNext")
        val eqName = User.Name("first", "last")
        val info = User.Info("test", name)
        User.new {
            age = 1
            this.info = info
        }

        val result = User.wrapRows(Users.selectAll())
        assertSame(result.first().info.name.firstName, "first")
        assertNotEquals(name, nextName)
        assertEquals(name, eqName)
        assertEquals(name, name)
    }
}
