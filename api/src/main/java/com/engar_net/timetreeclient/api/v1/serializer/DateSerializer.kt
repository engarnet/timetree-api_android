package com.engar_net.timetreeclient.api.v1.serializer

import kotlinx.serialization.*
import java.text.SimpleDateFormat
import java.util.*

@Serializer(forClass = Date::class)
object DateSerializer : KSerializer<Date> {
    private const val Iso8601Format = "yyyy-MM-dd'T'HH:mm:ss.SSSX"

    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeString(SimpleDateFormat(Iso8601Format).format(value))
    }

    override fun deserialize(decoder: Decoder): Date {
        return SimpleDateFormat(Iso8601Format).parse(decoder.decodeString())
    }
}