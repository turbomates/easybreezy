package io.easybreezy.infrastructure.exposed.dao

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlin.properties.ReadOnlyProperty

open class PrivateEntityClass<ID : Comparable<ID>, out T : Entity<ID>>(private val base: EntityClass<ID, T>) {

    open fun new(init: T.() -> Unit) = base.new(null, init)

    internal operator fun get(id: EntityID<ID>): T = base[id]

    internal operator fun get(id: ID): T = base[id]

    infix fun <REF : Comparable<REF>> referencedOn(column: Column<REF>) = base.referencedOn(column)

    infix fun <REF : Comparable<REF>> optionalReferencedOn(column: Column<REF?>) = base.optionalReferencedOn(column)

    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.backReferencedOn(
        column: Column<REF>
    ): ReadOnlyProperty<Entity<TargetID>, Target> {
        return base.backReferencedOn(column)
    }

    @JvmName("backReferencedOnOpt")
    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.backReferencedOn(
        column: Column<REF?>
    ): ReadOnlyProperty<Entity<TargetID>, Target> {
        return base.backReferencedOn(column)
    }

    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.optionalBackReferencedOn(
        column: Column<REF>
    ): OptionalBackReference<TargetID, Target, TargetID, Entity<TargetID>, REF> {
        return base.optionalBackReferencedOn(column)
    }

    @JvmName("optionalBackReferencedOnOpt")
    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.optionalBackReferencedOn(
        column: Column<REF?>
    ): OptionalBackReference<TargetID, Target, TargetID, Entity<TargetID>, REF> {
        return base.optionalBackReferencedOn(column)
    }

    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.referrersOn(
        column: Column<REF>
    ): Referrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return base.referrersOn(column)
    }


    fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.referrersOn(
        column: Column<REF>,
        cache: Boolean
    ): Referrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return base.referrersOn(column, cache)
    }

    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.optionalReferrersOn(
        column: Column<REF?>
    ): OptionalReferrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return base.optionalReferrersOn(column)
    }

    fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> PrivateEntityClass<TargetID, Target>.optionalReferrersOn(
        column: Column<REF?>,
        cache: Boolean = false
    ): OptionalReferrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return base.optionalReferrersOn(column, cache)
    }

    infix fun <TID : Comparable<TID>, Target : Entity<TID>> PrivateEntityClass<TID, Target>.via(table: Table): InnerTableLink<ID, Entity<ID>, TID, Target> =
        InnerTableLink(table, this@via.base)

    fun <TID : Comparable<TID>, Target : Entity<TID>> PrivateEntityClass<TID, Target>.via(
        sourceColumn: Column<EntityID<ID>>,
        targetColumn: Column<EntityID<TID>>
    ) =
        InnerTableLink(sourceColumn.table, this@via.base, sourceColumn, targetColumn)

    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.backReferencedOn(
        column: Column<REF?>
    ): ReadOnlyProperty<Entity<TargetID>, Target> {
        return backReferencedOn(column)
    }

    @JvmName("baseBackReferencedOnOpt")
    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.backReferencedOn(
        column: Column<REF>
    ): ReadOnlyProperty<Entity<TargetID>, Target> {
        return backReferencedOn(column)
    }

    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.optionalBackReferencedOn(
        column: Column<REF>
    ): OptionalBackReference<TargetID, Target, TargetID, Entity<TargetID>, REF> {
        return optionalBackReferencedOn(column)
    }

    @JvmName("baseOptionalBackReferencedOnOpt")
    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.optionalBackReferencedOn(
        column: Column<REF?>
    ): OptionalBackReference<TargetID, Target, TargetID, Entity<TargetID>, REF> {
        return optionalBackReferencedOn(column)
    }

    infix fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.referrersOn(
        column: Column<REF>
    ): Referrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return referrersOn(column)
    }

    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.referrersOn(
        column: Column<REF>,
        cache: Boolean
    ): Referrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return referrersOn(column, cache)
    }

    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.optionalReferrersOn(
        column: Column<REF?>
    ): OptionalReferrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return optionalReferrersOn(column)
    }

    private fun <TargetID : Comparable<TargetID>, Target : Entity<TargetID>, REF : Comparable<REF>> EntityClass<TargetID, Target>.optionalReferrersOn(
        column: Column<REF?>,
        cache: Boolean = false
    ): OptionalReferrers<TargetID, Entity<TargetID>, TargetID, Target, REF> {
        return optionalReferrersOn(column, cache)
    }

}


