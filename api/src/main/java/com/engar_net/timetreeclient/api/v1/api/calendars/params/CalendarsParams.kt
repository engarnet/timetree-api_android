package com.engar_net.timetreeclient.api.v1.api.calendars.params

import com.engar_net.timetreeclient.api.v1.entity.ext.toParam
import com.engar_net.timetreeclient.model.type.Include

data class CalendarsParams(val include: Include.Calendars?) {
    fun toQueryParams(): Map<String, String> {
        return include?.let {
            mapOf(
                "include" to it.toParam()
            )
        } ?: mapOf()
    }
}