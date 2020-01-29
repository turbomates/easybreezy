package io.easybreezy.user.api.controller

import com.google.gson.Gson
import io.easybreezy.user.TestEngine
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.UUID

class UserControllerTest {

    @Test
    fun testUserInvite() {
        val engine = TestEngine.create(UUID.randomUUID())

                with(engine.handleRequest(HttpMethod.Get, "/api/users")) {
                    println(response.content)
                    Assertions.assertEquals(response.status(), HttpStatusCode.OK)
                }

        with(engine.handleRequest(HttpMethod.Post, "/api/users/invite") {
            addHeader("Content-Type", "application/json")
            setBody(
                Gson().toJson(
                    mapOf(
                        "email" to "testadmin@testadmin.my",
                        "role" to "MEMBER"
                    )
                )
            )
        }) {
            println(response.content)
            Assertions.assertEquals(response.status(), HttpStatusCode.OK)
        }

        with(engine.handleRequest(HttpMethod.Get, "/api/users")) {
            println(response.content)
            Assertions.assertEquals(response.status(), HttpStatusCode.OK)
        }
    }

    @Test
    fun testUserInviteTwice() {
        val engine = TestEngine.create(UUID.randomUUID())

        with(engine.handleRequest(HttpMethod.Post, "/api/users/invite") {
            addHeader("Content-Type", "application/json")
            setBody(
                Gson().toJson(
                    mapOf(
                        "email" to "testadmin@testadmin.my",
                        "role" to "MEMBER"
                    )
                )
            )
        }) {
            Assertions.assertEquals(response.status(), HttpStatusCode.OK)
        }

        with(engine.handleRequest(HttpMethod.Post, "/api/users/invite") {
            addHeader("Content-Type", "application/json")
            setBody(
                Gson().toJson(
                    mapOf(
                        "email" to "testadmin@testadmin.my",
                        "role" to "MEMBER"
                    )
                )
            )
        }) {
            Assertions.assertTrue(response.content?.contains("errors") ?: false)
            Assertions.assertEquals(response.status(), HttpStatusCode.UnprocessableEntity)
        }
    }
}
