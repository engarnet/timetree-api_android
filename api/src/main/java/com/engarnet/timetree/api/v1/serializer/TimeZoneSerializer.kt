package com.engarnet.timetree.api.v1.serializer

import kotlinx.serialization.*
import java.util.*

@Serializer(forClass = TimeZone::class)
object TimeZoneSerializer : KSerializer<TimeZone> {
    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: TimeZone) {
        encoder.encodeString(value.id)
    }

    override fun deserialize(decoder: Decoder): TimeZone {
        return TimeZone.getTimeZone(decoder.decodeString())
    }
}