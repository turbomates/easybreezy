package io.easybreezy.application.db.test

import io.easybreezy.enums.UserStatus
import io.easybreezy.tables.Users.USERS
import io.easybreezy.tables.records.UsersRecord
import java.sql.Connection
import java.util.UUID

class UserRecordsFactory(connection: Connection) : RecordsFactory(connection) {
    fun createAdmin(configure: UsersRecord.() -> Unit = {}): UsersRecord {
        return createRecord(USERS) {
            id = UUID.randomUUID()
            password = "123"
            emailAddress = "admin-test@admin-test.my"
            firstName = "admin-test"
            lastName = "admin-test"
            status = UserStatus.ACTIVE
            configure()
        }
    }
}
