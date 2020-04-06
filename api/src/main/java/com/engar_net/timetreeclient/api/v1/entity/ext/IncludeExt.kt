package com.engar_net.timetreeclient.api.v1.entity.ext

import com.engar_net.timetreeclient.model.type.Include

fun Include.Calendars.toParam(): String {
    return listOf(
        if (labels) "labels" else "",
        if (members) "members" else ""
    ).filter { it != "" }.joinToString(",")
}

fun Include.Events.toParam(): String {
    return listOf(
        if (labels) "labels" else "",
        if (creator) "creator" else "",
        if (attendees) "attendees" else ""
    ).filter { it != "" }.joinToString(",")
}