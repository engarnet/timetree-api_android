package com.engarnet.timetree.api.v1.serializer

import android.net.Uri
import kotlinx.serialization.*

@Serializer(forClass = Uri::class)
object UriSerializer : KSerializer<Uri> {

    override val descriptor: SerialDescriptor =
        PrimitiveDescriptor("WithCustomDefault", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uri {
        return Uri.parse(decoder.decodeString())
    }
}