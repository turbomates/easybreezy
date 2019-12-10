package io.easybreezy.application.db.jooq

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import org.jooq.Binding
import org.jooq.BindingGetResultSetContext
import org.jooq.BindingGetSQLInputContext
import org.jooq.BindingGetStatementContext
import org.jooq.BindingRegisterContext
import org.jooq.BindingSQLContext
import org.jooq.BindingSetSQLOutputContext
import org.jooq.BindingSetStatementContext
import org.jooq.Converter
import org.jooq.conf.ParamType
import org.jooq.impl.DSL
import java.sql.SQLException
import java.sql.SQLFeatureNotSupportedException
import java.sql.Types
import java.util.Objects

class PostgresJSONGsonBinding : Binding<Any, JsonElement> {

    // The converter does all the work
    override fun converter(): Converter<Any, JsonElement> {
        return object : Converter<Any, JsonElement> {
            override fun from(t: Any?): JsonElement {
                return if (t == null) JsonNull.INSTANCE else Gson().fromJson("" + t, JsonElement::class.java)
            }

            override fun to(u: JsonElement?): Any? {
                return if (u == null || u === JsonNull.INSTANCE) null else Gson().toJson(u)
            }

            override fun fromType(): Class<Any> {
                return Any::class.java
            }

            override fun toType(): Class<JsonElement> {
                return JsonElement::class.java
            }
        }
    }

    // Rending a bind variable for the binding context's value and casting it to the json type
    @Throws(SQLException::class)
    override fun sql(ctx: BindingSQLContext<JsonElement>) {
        // Depending on how you generate your SQL, you may need to explicitly distinguish
        // between jOOQ generating bind variables or inlined literals.
        if (ctx.render().paramType() == ParamType.INLINED)
            ctx.render().visit(DSL.inline(ctx.convert(converter()).value())).sql("::jsonb")
        else
            ctx.render().sql("?::jsonb")
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Throws(SQLException::class)
    override fun register(ctx: BindingRegisterContext<JsonElement>) {
        ctx.statement().registerOutParameter(ctx.index(), Types.VARCHAR)
    }

    // Converting the JsonElement to a String value and setting that on a JDBC PreparedStatement
    @Throws(SQLException::class)
    override fun set(ctx: BindingSetStatementContext<JsonElement>) {
        ctx.statement().setString(ctx.index(), Objects.toString(ctx.convert(converter()).value(), null))
    }

    // Getting a String value from a JDBC ResultSet and converting that to a JsonElement
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetResultSetContext<JsonElement>) {
        ctx.convert(converter()).value(ctx.resultSet().getString(ctx.index()))
    }

    // Getting a String value from a JDBC CallableStatement and converting that to a JsonElement
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetStatementContext<JsonElement>) {
        ctx.convert(converter()).value(ctx.statement().getString(ctx.index()))
    }

    // Setting a value on a JDBC SQLOutput (useful for Oracle OBJECT types)
    @Throws(SQLException::class)
    override fun set(ctx: BindingSetSQLOutputContext<JsonElement>) {
        throw SQLFeatureNotSupportedException()
    }

    // Getting a value from a JDBC SQLInput (useful for Oracle OBJECT types)
    @Throws(SQLException::class)
    override fun get(ctx: BindingGetSQLInputContext<JsonElement>) {
        throw SQLFeatureNotSupportedException()
    }
}
