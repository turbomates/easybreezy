package io.easybreezy.application.db.test

import org.jooq.Table
import org.jooq.UpdatableRecord
import org.jooq.impl.DSL
import java.sql.Connection

open class RecordsFactory(protected val connection: Connection) {
    fun <TRecord : UpdatableRecord<TRecord>> createRecord(
        table: Table<TRecord>,
        configure: TRecord.() -> Unit = {}
    ): TRecord {
        DSL.using(connection).use {
            val record = it.newRecord(table).apply(configure)
            record.insert()
            record.refresh()
            return record
        }
    }
}
