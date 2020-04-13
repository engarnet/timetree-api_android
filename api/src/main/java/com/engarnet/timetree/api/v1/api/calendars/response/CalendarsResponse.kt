package com.engarnet.timetree.api.v1.api.calendars.response

import android.net.Uri
import com.engarnet.timetree.api.v1.entity.CalendarEntity
import com.engarnet.timetree.api.v1.entity.IncludedEntity
import com.engarnet.timetree.model.TCalendar
import com.engarnet.timetree.model.TLabel
import com.engarnet.timetree.model.TUser
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class CalendarsResponse(
    val data: List<CalendarEntity>,
    val included: List<IncludedEntity> = listOf()
)

internal fun CalendarsResponse.toModel(): List<TCalendar> {
    return data.map { entity ->
        object : TCalendar {
            override val id: String get() = entity.id
            override val name: String get() = entity.attributes.name
            override val description: String get() = entity.attributes.description
            override val color: Int get() = entity.attributes.color
            override val order: Int get() = entity.attributes.order
            override val imageUrl: Uri? get() = entity.attributes.imageUrl
            override val createdAt: Date get() = entity.attributes.createdAt
            override val labels: List<TLabel>
                get() = entity.relationships.labels.data
                    .map { it.id }
                    .mapNotNull { id -> included.firstOrNull { it.id == id } }
                    .map { data ->
                        object : TLabel {
                            override val id: String get() = data.id
                            override val name: String get() = data.attributes.name
                            override val color: Int get() = data.attributes.color!!
                        }
                    }
            override val members: List<TUser>
                get() = entity.relationships.members.data
                    .map { it.id }
                    .mapNotNull { id -> included.firstOrNull { it.id == id } }
                    .map { data ->
                        object : TUser {
                            override val id: String get() = data.id
                            override val name: String get() = data.attributes.name
                            override val description: String get() = data.attributes.description
                            override val imageUrl: Uri? get() = data.attributes.imageUrl
                        }
                    }
        }
    }
}