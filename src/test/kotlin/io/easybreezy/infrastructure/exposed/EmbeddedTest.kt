package io.easybreezy.infrastructure.exposed

import io.easybreezy.infrastructure.exposed.dao.Embeddable
import io.easybreezy.infrastructure.exposed.dao.EmbeddableClass
import io.easybreezy.infrastructure.exposed.dao.EmbeddableTable
import io.easybreezy.infrastructure.exposed.dao.Entity
import io.easybreezy.infrastructure.exposed.dao.embedded
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.Test
import kotlin.test.assertSame

object Accounts : IntIdTable() {
    val name = varchar("account", 255).nullable()
    val balance = embedded<Money>(MoneyTable)
    val latBalance = embedded<Money>(MoneyTable, "last")
}

object MoneyTable : EmbeddableTable() {
    val amount = integer("amount")
    val currency = embedded<Currency>(CurrencyTable)
}

object CurrencyTable : EmbeddableTable() {
    val name = varchar("name", 25)
}

class Currency : Embeddable() {
    var name by CurrencyTable.name
}

class Money private constructor() : Embeddable() {
    var amount by MoneyTable.amount
    var currency by MoneyTable.currency

    companion object : EmbeddableClass<Money>(Money::class) {
        override fun createInstance(): Money {
            return Money()
        }
    }
}

class Account(id: EntityID<Int>) : Entity<Int>(id) {
    companion object : EntityClass<Int, Account>(Accounts)

    var name by Accounts.name
    var balance by Accounts.balance
    var lastBalance by Accounts.latBalance
}

@Test
fun main() {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")

    transaction {
        SchemaUtils.create(Accounts)
        val money = Money.createInstance()
        money.amount = 10
        val currency = Currency()
        currency.name = "EUR"
        money.currency = currency
        val last = Money.createInstance()
        last.amount = 20
        last.currency = currency
        Account.new {
            name = "test"
            balance = money
            lastBalance = last
        }
        var result = Account.wrapRows(Accounts.selectAll()).first()
        assertSame(10, result.balance.amount)
        assertSame("EUR", result.balance.currency.name)
        result.lastBalance.amount = 40
        val newCurrency = Currency()
        newCurrency.name = "TEST"
        result.lastBalance.currency = newCurrency
        result = Account.wrapRows(Accounts.selectAll()).first()
        assertSame("TEST", result.lastBalance.currency.name)
        assertSame(40, result.lastBalance.amount)
    }
}
