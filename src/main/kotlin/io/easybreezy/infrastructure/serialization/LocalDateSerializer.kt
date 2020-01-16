package io.easybreezy.infrastructure.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Serializer(forClass = LocalDate::class)
object LocalDateSerializer : KSerializer<LocalDate> {
    private val df: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")

    override fun serialize(output: Encoder, obj: LocalDate) {
        output.encodeString(obj.format(df))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString())
    }
}

@Serializer(forClass = LocalDateTime::class)
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val df: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss.SSS")

    override fun serialize(output: Encoder, obj: LocalDateTime) {
        output.encodeString(obj.format(df))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), df)
    }
}

