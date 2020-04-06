package com.engar_net.timetreeclient.wrapper

import com.engar_net.timetreeclient.api.client.ApiClient
import com.engar_net.timetreeclient.api.client.impl.DefaultApiClient
import com.engar_net.timetreeclient.api.v1.api.activities.ActivitiesApi
import com.engar_net.timetreeclient.api.v1.api.activities.params.AddActivityParams
import com.engar_net.timetreeclient.api.v1.api.calendars.CalendarApi
import com.engar_net.timetreeclient.api.v1.api.calendars.params.CalendarLabelsParams
import com.engar_net.timetreeclient.api.v1.api.calendars.params.CalendarMembersParams
import com.engar_net.timetreeclient.api.v1.api.calendars.params.CalendarParams
import com.engar_net.timetreeclient.api.v1.api.calendars.params.CalendarsParams
import com.engar_net.timetreeclient.api.v1.api.events.EventsApi
import com.engar_net.timetreeclient.api.v1.api.events.params.*
import com.engar_net.timetreeclient.api.v1.api.user.UserApi
import com.engar_net.timetreeclient.model.TCalendar
import com.engar_net.timetreeclient.model.TEvent
import com.engar_net.timetreeclient.model.TLabel
import com.engar_net.timetreeclient.model.TUser

class TimeTreeApiWrapper(
    accessToken: String,
    apiClient: ApiClient = DefaultApiClient()
) {
    private val userApi: UserApi = UserApi(apiClient)
    private val calendarApi: CalendarApi = CalendarApi(apiClient)
    private val eventsApi: EventsApi = EventsApi(apiClient)
    private val activitiesApi: ActivitiesApi = ActivitiesApi(apiClient)

    init {
        apiClient.accessToken = accessToken
    }

    suspend fun user(): TUser = userApi.user()

    suspend fun calendars(params: CalendarsParams): List<TCalendar> = calendarApi.calendars(params)
    suspend fun calendar(params: CalendarParams): TCalendar = calendarApi.calendar(params)
    suspend fun calendarLabels(params: CalendarLabelsParams): List<TLabel> =
        calendarApi.calendarLabels(params)

    suspend fun calendarMembers(params: CalendarMembersParams): List<TUser> =
        calendarApi.calendarMembers(params)

    suspend fun events(params: EventsParams): TEvent = eventsApi.events(params)
    suspend fun upcomingEvents(params: UpcomingEventsParams): List<TEvent> =
        eventsApi.upcomingEvents(params)

    suspend fun addEvent(params: AddEventsParams): TEvent =
        eventsApi.addEvent(params)

    suspend fun updateEvent(params: UpdateEventParams): TEvent =
        eventsApi.updateEvent(params)

    suspend fun deleteEvent(params: DeleteEventsParams) =
        eventsApi.deleteEvent(params)

    suspend fun addActivity(params: AddActivityParams) =
        activitiesApi.addActivity(params)
}