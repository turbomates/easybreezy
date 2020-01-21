package io.easybreezy.project.model.team

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class TeamRepository : Team.Repository() {
    suspend fun test() = withContext(Dispatchers.IO) {
        findById(UUID.randomUUID())
    }
}