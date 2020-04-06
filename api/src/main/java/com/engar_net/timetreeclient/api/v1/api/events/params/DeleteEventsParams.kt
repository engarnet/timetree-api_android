package com.engar_net.timetreeclient.api.v1.api.events.params

data class DeleteEventsParams(
    val calendarId: String,
    val eventId: String
)