package com.engar_net.timetreeclient.api.v1.api.events.params

import com.engar_net.timetreeclient.api.v1.entity.ext.toParam
import com.engar_net.timetreeclient.model.type.Include

data class EventsParams(
    val calendarId: String,
    val eventId: String,
    val include: Include.Events?
) {
    fun toQueryParams(): Map<String, String> {
        return include?.let {
            mapOf(
                "include" to it.toParam()
            )
        } ?: mapOf()
    }
}
