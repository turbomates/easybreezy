package io.easybreezy.application.db.hibernate.type

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonSerializer
import com.google.gson.reflect.TypeToken
import org.hibernate.type.AbstractSingleColumnStandardBasicType
import org.hibernate.type.descriptor.ValueBinder
import org.hibernate.type.descriptor.ValueExtractor
import org.hibernate.type.descriptor.WrapperOptions
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor
import org.hibernate.type.descriptor.java.JavaTypeDescriptor
import org.hibernate.type.descriptor.sql.BasicBinder
import org.hibernate.type.descriptor.sql.BasicExtractor
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor
import org.hibernate.usertype.DynamicParameterizedType
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types
import java.util.Properties
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

class JsonBType : DynamicParameterizedType,
    AbstractSingleColumnStandardBasicType<Any>(JsonSQLDescriptor.INSTANCE, JsonBTypeDescriptor()) {
    companion object {
        const val ADAPTER_PROPERTY = "adapter"
    }

    override fun setParameterValues(parameters: Properties) {
        (javaTypeDescriptor as JsonBTypeDescriptor).setParameterValues(parameters)
    }

    override fun getName(): String {
        return "jsonb"
    }
}

class JsonBTypeDescriptor : DynamicParameterizedType, AbstractTypeDescriptor<Any>(Any::class.java) {
    private var type: Class<*>? = null
    private var adapter: Any? = null
    val serializer: Gson by lazy {
        val builder = GsonBuilder()
        adapter?.let {
            builder.registerTypeHierarchyAdapter(type, it)
        }
        builder.create()
    }

    override fun setParameterValues(parameters: Properties) {
        parameters[JsonBType.ADAPTER_PROPERTY]?.let {
            val kClass = Class.forName(it.toString()).kotlin
            if (kClass.isSubclassOf(JsonSerializer::class) || kClass.isSubclassOf(JsonDeserializer::class)) {
                adapter = kClass.createInstance()
            }
        }
        type =
            (parameters[DynamicParameterizedType.PARAMETER_TYPE] as DynamicParameterizedType.ParameterType).returnedClass
    }

    override fun fromString(string: String): Any {
        return serializer.fromJson(string, TypeToken.get(type).type)
    }

    override fun <X : Any> unwrap(value: Any?, type: Class<X>?, options: WrapperOptions?): X? {
        if (value == null) {
            return null
        }
        if (Any::class.java.isAssignableFrom(javaType)) {
            return serializer.toJsonTree(value) as X
        }
        throw unknownUnwrap(type)
    }

    override fun <X : Any?> wrap(value: X, options: WrapperOptions?): Any? {
        return if (value == null) {
            null
        } else fromString(value.toString())
    }
}

class JsonSQLDescriptor : SqlTypeDescriptor {
    override fun <X : Any?> getBinder(javaTypeDescriptor: JavaTypeDescriptor<X>?): ValueBinder<X> {
        return object : BasicBinder<X>(javaTypeDescriptor, this) {
            override fun doBind(st: PreparedStatement, value: X, index: Int, options: WrapperOptions?) {
                st.setObject(index, javaDescriptor.unwrap(value, JsonElement::class.java, options), sqlType)
            }

            override fun doBind(st: CallableStatement, value: X, name: String?, options: WrapperOptions?) {
                st.setObject(name, javaDescriptor.unwrap(value, JsonElement::class.java, options), sqlType)
            }
        }
    }

    override fun canBeRemapped(): Boolean {
        return true
    }

    override fun getSqlType(): Int {
        return Types.OTHER
    }

    override fun <X : Any?> getExtractor(javaTypeDescriptor: JavaTypeDescriptor<X>?): ValueExtractor<X> {
        return object : BasicExtractor<X>(javaTypeDescriptor, this) {
            @Throws(SQLException::class)
            override fun doExtract(rs: ResultSet, name: String, options: WrapperOptions): X {
                return javaDescriptor.wrap(rs.getObject(name), options)
            }

            @Throws(SQLException::class)
            override fun doExtract(statement: CallableStatement, index: Int, options: WrapperOptions): X {
                return javaDescriptor.wrap(statement.getObject(index), options)
            }

            @Throws(SQLException::class)
            override fun doExtract(statement: CallableStatement, name: String, options: WrapperOptions): X {
                return javaDescriptor.wrap(statement.getObject(name), options)
            }
        }
    }

    companion object {
        val INSTANCE = JsonSQLDescriptor()
    }
}
