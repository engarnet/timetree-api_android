package com.engarnet.timetree.api.v1.api.activities.response

import com.engarnet.timetree.api.v1.entity.ActivityEntity
import com.engarnet.timetree.model.TActivity
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
internal data class ActivityResponse(
    val data: ActivityEntity
)

internal fun ActivityResponse.toModel(): TActivity {
    return data.let {
        object : TActivity {
            override val id: String get() = it.id
            override val content: String get() = it.attributes.content
            override val updatedAt: Date get() = it.attributes.updatedAt
            override val createdAt: Date get() = it.attributes.createdAt
        }
    }
}