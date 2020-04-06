package com.engar_net.timetreeclient.api.v1.api.events.response

import android.net.Uri
import com.engar_net.timetreeclient.api.v1.entity.EventEntity
import com.engar_net.timetreeclient.api.v1.entity.IncludedEntity
import com.engar_net.timetreeclient.model.TEvent
import com.engar_net.timetreeclient.model.TLabel
import com.engar_net.timetreeclient.model.TUser
import com.engar_net.timetreeclient.model.type.Category
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class EventResponse(
    val data: EventEntity,
    val included: List<IncludedEntity> = listOf() // TODO: 仕様にはないレスポンス
)

internal fun EventResponse.toModel(): TEvent {
    return data.let { entity ->
        object : TEvent {
            override val id: String get() = entity.id
            override val category: Category get() = entity.attributes.category
            override val title: String get() = entity.attributes.title
            override val allDay: Boolean get() = entity.attributes.allDay
            override val startAt: Date get() = entity.attributes.startAt
            override val startTimezone: TimeZone get() = entity.attributes.startTimezone
            override val endAt: Date get() = entity.attributes.endAt
            override val endTimezone: TimeZone get() = entity.attributes.endTimezone
            override val recurrence: List<Unit>? get() = entity.attributes.recurrence
            override val recurringUuid: String? get() = entity.attributes.recurringUuid
            override val description: String? get() = entity.attributes.description
            override val location: String? get() = entity.attributes.location
            override val url: Uri? get() = entity.attributes.url
            override val creator: TUser
                get() {
                    return entity.relationships.creator.data.id.let {
                        id
                        included.first { it.id == id }.let {
                            object : TUser {
                                override val id: String get() = it.id
                                override val name: String get() = it.attributes.name
                                override val description: String get() = it.attributes.description
                                override val imageUrl: Uri? get() = it.attributes.imageUrl
                            }
                        }
                    }
                }
            override val label: TLabel
                get() {
                    return entity.relationships.label.data.id.let { id ->
                        included.first { it.id == id }.let {
                            object : TLabel {
                                override val id: String get() = it.id
                                override val name: String get() = it.attributes.name
                                override val color: Int get() = it.attributes.color!!
                            }
                        }
                    }
                }
            override val attendees: List<TUser>
                get() {
                    return data.relationships.attendees.data.map { entity ->
                        entity.id
                    }.map { attendeesId ->
                        included.first { it.id == attendeesId }.let {
                            object : TUser {
                                override val id: String get() = it.id
                                override val name: String get() = it.attributes.name
                                override val description: String get() = it.attributes.description
                                override val imageUrl: Uri? get() = it.attributes.imageUrl
                            }
                        }
                    }
                }
            override val createdAt: Date get() = entity.attributes.createdAt
            override val updatedAt: Date get() = entity.attributes.updatedAt
        }
    }
}