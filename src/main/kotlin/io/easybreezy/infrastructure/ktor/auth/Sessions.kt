package io.easybreezy.infrastructure.ktor.auth

import io.easybreezy.infrastructure.serialization.UUIDSerializer
import io.easybreezy.user.model.Role
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.util.UUID
import io.ktor.sessions.SessionSerializer as KtorSessionSerializer

@Serializable
data class Session(
    val principal: UserPrincipal? = null,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    val ttl: Int = 3600
)

@Serializable
data class UserPrincipal(
    @Serializable(with = UUIDSerializer::class)
    override val id: UUID,
    val roles: Set<Role>
) : Principal

interface Principal : io.ktor.auth.Principal {
    val id: UUID
}

class SessionSerializer : KtorSessionSerializer<Session> {
    val serializer: Json = Json(configuration = JsonConfiguration(useArrayPolymorphism = true))

    override fun serialize(session: Session): String = serializer.stringify(Session.serializer(), session)
    override fun deserialize(text: String): Session = serializer.parse(Session.serializer(), text)
}
