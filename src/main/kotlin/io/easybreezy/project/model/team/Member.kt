package io.easybreezy.project.model.team

import io.easybreezy.infrastructure.exposed.dao.PrivateEntityClass
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import java.util.*
import kotlin.reflect.KProperty

class Member private constructor(id: EntityID<UUID>) : UUIDEntity(id) {
    private var user by Members.user
    private var role by Role referencedOn Members.role
    private var info by Members.info


    class Info(name: String, username: String, avatar: String) : Embeddable() {
        private var name by Members.Info.name
        private var username by Members.Info.username
        private var avatar by Members.Info.avatar

        init {
            this.name = name
            this.username = username
            this.avatar = avatar
        }
    }

    companion object : PrivateEntityClass<UUID, Member>(MemberRepository) {
        fun create(user: UUID, role: Role, info: Info): Member {
            return Member.new {
                this.user = user
                this.role = role
                this.info = info
            }
        }

    }

    abstract class Repository : UUIDEntityClass<Member>(Members, Member::class.java) {
        protected override fun createInstance(entityId: EntityID<UUID>, row: ResultRow?): Member {
            return Member(entityId)
        }
    }
}


object MemberRepository : Member.Repository() {

}

object Members : UUIDTable() {
    val user = uuid("user_id")
    val role = reference("roles", Roles)
    val name = varchar("name", 25)
    val info = Info

    object Info : EmbeddableColumns() {
        val name = varchar("info_name", 25).nullable()
        val username = varchar("info_username", 25).nullable()
        val avatar = varchar("info_avatar", 25).nullable()
    }
}

open class Embeddable {
    internal val writeValues = LinkedHashMap<Column<Any?>, Any?>()
    internal var readValues: ResultRow? = null

    operator fun <T> Column<T>.getValue(info: Embeddable, property: KProperty<*>): T? {
        return when {
            writeValues.containsKey(this as Column<out Any?>) -> writeValues[this as Column<out Any?>] as T
            columnType.nullable -> readValues?.get(this)
            else -> readValues?.get(this)
        }
    }

    operator fun <T> Column<T>.setValue(info: Embeddable, property: KProperty<*>, value: Any?) {
        val currentValue = readValues?.getOrNull(this)
        if (writeValues.containsKey(this as Column<out Any?>) || currentValue != value) {
            writeValues[this as Column<Any?>] = value
        }
    }
}

open class EmbeddableColumns {
    operator fun getValue(embeddable: Entity<*>, property: KProperty<*>): Any {
        return ""
    }

    operator fun setValue(embeddable: Entity<*>, property: KProperty<*>, any: Any) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

