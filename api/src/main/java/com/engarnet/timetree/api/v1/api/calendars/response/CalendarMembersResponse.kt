package com.engarnet.timetree.api.v1.api.calendars.response

import android.net.Uri
import com.engarnet.timetree.api.v1.entity.UserEntity
import com.engarnet.timetree.model.TUser
import kotlinx.serialization.Serializable

@Serializable
internal data class CalendarMembersResponse(
    val data: List<UserEntity>
)

internal fun CalendarMembersResponse.toModel(): List<TUser> {
    return data.map {
        object : TUser {
            override val id: String get() = it.id
            override val name: String get() = it.attributes.name
            override val description: String get() = it.attributes.description
            override val imageUrl: Uri? get() = it.attributes.imageUrl
        }
    }
}