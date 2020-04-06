package com.engar_net.timetreeclient.model.type

sealed class Include {
    data class Calendars(val labels: Boolean, val members: Boolean)
    data class Events(val labels: Boolean, val creator: Boolean, val attendees: Boolean)
}