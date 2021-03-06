package com.engarnet.timetree.api.v1.api.user.response

import android.net.Uri
import com.engarnet.timetree.api.v1.entity.UserEntity
import com.engarnet.timetree.model.TUser
import kotlinx.serialization.Serializable

@Serializable
internal data class UserResponse(
    val data: UserEntity
)

internal fun UserResponse.toModel(): TUser {
    return object : TUser {
        override val id: String get() = data.id
        override val name: String get() = data.attributes.name
        override val description: String get() = data.attributes.description
        override val imageUrl: Uri? get() = data.attributes.imageUrl
    }
}