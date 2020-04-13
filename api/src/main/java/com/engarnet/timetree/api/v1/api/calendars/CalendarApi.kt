package com.engarnet.timetree.api.v1.api.calendars

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarLabelsParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarMembersParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarParams
import com.engarnet.timetree.api.v1.api.calendars.params.CalendarsParams
import com.engarnet.timetree.api.v1.api.calendars.response.*
import com.engarnet.timetree.model.TCalendar
import com.engarnet.timetree.model.TLabel
import com.engarnet.timetree.model.TUser
import kotlinx.serialization.json.Json

class CalendarApi(private val apiClient: ApiClient) {
    suspend fun calendars(params: CalendarsParams): List<TCalendar> {
        val path = Config.apiUrl + "/calendars"
        val response = apiClient.get(path, params.toQueryParams())
        print("response: $response")
        return Json.parse(CalendarsResponse.serializer(), response).toModel()
    }

    suspend fun calendar(params: CalendarParams): TCalendar {
        val path = Config.apiUrl + "/calendars/" + params.calendarId
        val response = apiClient.get(path, params.toQueryParams())
        print("response: $response")
        return Json.parse(CalendarResponse.serializer(), response).toModel()
    }

    suspend fun calendarLabels(params: CalendarLabelsParams): List<TLabel> {
        val path = Config.apiUrl + "/calendars/" + params.calendarId + "/labels"
        val response = apiClient.get(path)
        print("response: $response")
        return Json.parse(CalendarLabelsResponse.serializer(), response).toModel()
    }

    suspend fun calendarMembers(params: CalendarMembersParams): List<TUser> {
        val path = Config.apiUrl + "/calendars/" + params.calendarId + "/members"
        val response = apiClient.get(path)
        print("response: $response")
        return Json.parse(CalendarMembersResponse.serializer(), response).toModel()
    }
}