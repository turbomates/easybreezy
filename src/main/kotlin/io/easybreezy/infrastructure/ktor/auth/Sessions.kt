package io.easybreezy.infrastructure.ktor.auth

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.easybreezy.user.model_legacy.User
import io.ktor.sessions.SessionSerializer
import java.lang.reflect.Type
import java.util.UUID

data class Session(val principal: UserPrincipal? = null, val attributes: MutableMap<String, String> = mutableMapOf(), val ttl: Int = 3600)

data class UserPrincipal(override val id: UUID, val roles: Set<User.Role>) : Principal

interface Principal : io.ktor.auth.Principal {
    val id: UUID
}

class GsonSessionSerializer(
    private val type: Type,
    builder: GsonBuilder = GsonBuilder(),
    configure: GsonBuilder.() -> Unit = {}
) : SessionSerializer {
    private var gson: Gson

    init {
        configure(builder)
        gson = builder.create()
    }

    override fun serialize(session: Any): String = gson.toJson(session)
    override fun deserialize(text: String): Any = gson.fromJson(text, type)
}
