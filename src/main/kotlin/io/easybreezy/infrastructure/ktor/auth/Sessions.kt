package io.easybreezy.infrastructure.ktor.auth

// import io.easybreezy.user.model_legacy.User
import io.easybreezy.user.model.Role
import java.util.*

data class Session(
    val principal: UserPrincipal? = null,
    val attributes: MutableMap<String, String> = mutableMapOf(),
    val ttl: Int = 3600
)

data class UserPrincipal(override val id: UUID, val roles: Set<Role>) : Principal

interface Principal : io.ktor.auth.Principal {
    val id: UUID
}

//object SessionSerializer : SessionSerializer<Principal> {
//    val serializer: Json = Json(configuration = JsonConfiguration(useArrayPolymorphism = true))
//    override fun serialize(session: Principal): String = gson.toJson(session)
//    override fun deserialize(text: String): Principal = gson.fromJson(text, type)
//}
