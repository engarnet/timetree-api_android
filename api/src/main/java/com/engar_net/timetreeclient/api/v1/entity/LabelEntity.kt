package com.engar_net.timetreeclient.api.v1.entity

import com.engar_net.timetreeclient.api.v1.serializer.ColorSerializer
import kotlinx.serialization.Serializable

@Serializable
data class LabelEntity(
    val id: String,
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val name: String,
        @Serializable(with = ColorSerializer::class) val color: Int
    )
}