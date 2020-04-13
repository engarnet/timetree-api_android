package com.engarnet.timetree.api.v1.api.events

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.events.params.*
import com.engarnet.timetree.api.v1.api.events.response.EventResponse
import com.engarnet.timetree.api.v1.api.events.response.UpcomingEventsResponse
import com.engarnet.timetree.api.v1.api.events.response.toModel
import com.engarnet.timetree.model.TEvent
import kotlinx.serialization.json.Json

class EventsApi(private val apiClient: ApiClient) {
    suspend fun events(params: EventsParams): TEvent {
        val path = Config.apiUrl + "/calendars/" + params.calendarId + "/events/" + params.eventId
        val response = apiClient.get(path, params.toQueryParams())
        print("response: $response")
        return Json.parse(EventResponse.serializer(), response).toModel()
    }

    suspend fun upcomingEvents(params: UpcomingEventsParams): List<TEvent> {
        val path = Config.apiUrl + "/calendars/" + params.calendarId + "/upcoming_events"
        val response = apiClient.get(path, params.toQueryParams())
        print("response: $response")
        return Json.parse(UpcomingEventsResponse.serializer(), response).toModel()
    }

    suspend fun addEvent(params: AddEventsParams): TEvent {
        val path = Config.apiUrl + "/calendars/" + params.calendarId + "/events"
        val response = apiClient.post(path, params.toJson())
        print("response: $response")
        return Json.parse(EventResponse.serializer(), response).toModel()
    }

    suspend fun updateEvent(params: UpdateEventParams): TEvent {
        val path =
            Config.apiUrl + "/calendars/" + params.addEventsParams.calendarId + "/events/" + params.eventId
        val response = apiClient.put(path, params.toJson())
        print("response: $response")
        return Json.parse(EventResponse.serializer(), response).toModel()
    }

    suspend fun deleteEvent(params: DeleteEventsParams) {
        val path = Config.apiUrl + "/calendars/" + params.calendarId + "/events/" + params.eventId
        val response = apiClient.delete(path)
        print("response: $response")
    }
}