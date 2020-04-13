package com.engarnet.timetree.model

import android.net.Uri
import com.engarnet.timetree.model.type.Category
import java.util.*

interface TEvent {
    val id: String
    val category: Category
    val title: String
    val allDay: Boolean
    val startAt: Date
    val startTimezone: TimeZone
    val endAt: Date
    val endTimezone: TimeZone
    val recurrence: List<Unit>?
    val recurringUuid: String?
    val description: String?
    val location: String?
    val url: Uri?
    val creator: TUser
    val label: TLabel
    val attendees: List<TUser>
    val createdAt: Date
    val updatedAt: Date
}