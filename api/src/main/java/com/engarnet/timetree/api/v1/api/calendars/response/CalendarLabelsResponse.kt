package com.engarnet.timetree.api.v1.api.calendars.response

import com.engarnet.timetree.api.v1.entity.LabelEntity
import com.engarnet.timetree.model.TLabel
import kotlinx.serialization.Serializable

@Serializable
internal data class CalendarLabelsResponse(val data: List<LabelEntity>)

internal fun CalendarLabelsResponse.toModel(): List<TLabel> {
    return data.map {
        object : TLabel {
            override val id: String get() = it.id
            override val name: String get() = it.attributes.name
            override val color: Int get() = it.attributes.color
        }
    }
}