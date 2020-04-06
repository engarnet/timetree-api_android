package com.engar_net.timetreeclient.api.v1.api.events.params

import com.engar_net.timetreeclient.api.v1.entity.ext.toParam
import com.engar_net.timetreeclient.model.type.Include
import java.util.*

data class UpcomingEventsParams(
    val calendarId: String,
    val timeZone: TimeZone?,
    val days: Int?,
    val include: Include.Events?
) {
    fun toQueryParams(): Map<String, String> {
        return listOfNotNull(
            if (timeZone != null) "timezone" to timeZone.toString() else null,
            if (days != null) "days" to days.toString() else null,
            if (include != null) "include" to include.toParam() else null
        ).map {
            it.first to it.second
        }.toMap()
    }
}