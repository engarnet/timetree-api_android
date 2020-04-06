package com.engar_net.timetreeclient.api

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
import com.engar_net.timetreeclient.api.v1.api.events.params.AddEventsParams
import com.engar_net.timetreeclient.api.v1.api.events.params.DeleteEventsParams
import com.engar_net.timetreeclient.api.v1.api.events.params.EventsParams
import com.engar_net.timetreeclient.api.v1.api.events.params.UpcomingEventsParams
import com.engar_net.timetreeclient.api.v1.api.events.params.UpdateEventParams
import com.engar_net.timetreeclient.api.v1.api.user.UserApi
import com.engar_net.timetreeclient.model.type.Include
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.*

// TODO: APIの疎通確認用に作ったコードなのでUnitTestではない
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ApiUnitTest {
    private lateinit var apiClient: ApiClient

    private var accessToken: String = ""
    private var calendarId: String = ""
    private var eventId: String = ""

    @Before
    fun setup() {
        apiClient = DefaultApiClient()
        apiClient.accessToken = accessToken
    }

    @Test
    fun test001_user() {
        runBlocking {
            val api = UserApi(apiClient)
            val result = api.user()
            print("result: ${result.id}")
        }
    }

    @Test
    fun test002_calendars() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarsParams(
                include = Include.Calendars(labels = true, members = true)
            ).runCatching {
                api.calendars(this)
            }.onSuccess {
                print("result: ${it.size}")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test003_calendar() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarParams(
                calendarId = calendarId,
                include = Include.Calendars(labels = true, members = true)
            ).runCatching {
                api.calendar(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test004_calendarLabels() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarLabelsParams(
                calendarId = calendarId
            ).runCatching {
                api.calendarLabels(this)
            }.onSuccess {
                print("result: ${it.size}")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test005_calendarMembers() {
        runBlocking {
            val api = CalendarApi(apiClient)

            CalendarMembersParams(
                calendarId = calendarId
            ).runCatching {
                api.calendarMembers(this)
            }.onSuccess {
                print("result: ${it.size}")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test006_events() {
        runBlocking {
            val api = EventsApi(apiClient)

            EventsParams(
                calendarId = calendarId,
                eventId = eventId,
                include = Include.Events(
                    labels = true,
                    creator = true,
                    attendees = true
                )
            ).runCatching {
                api.events(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test007_upcomingEvents() {
        runBlocking {
            val api = EventsApi(apiClient)

            UpcomingEventsParams(
                calendarId = calendarId,
                timeZone = TimeZone.getDefault(),
                days = 7,
                include = Include.Events(
                    labels = true,
                    creator = true,
                    attendees = true
                )
            ).runCatching {
                api.upcomingEvents(this)
            }.onSuccess {
                print("result: ${it.size}")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test008_addEvent() {
        runBlocking {
            val api = EventsApi(apiClient)

            AddEventsParams.AddScheduleParams(
                calendarId = calendarId,
                title = "UnitTestEvent",
                allDay = false,
                startAt = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 1)
                }.time,
                startTimeZone = TimeZone.getDefault(),
                endAt = Calendar.getInstance().apply {
                    add(Calendar.DAY_OF_YEAR, 2)
                }.time,
                endTimeZone = TimeZone.getDefault(),
                description = "UnitTestEvent's description",
                location = "大阪駅",
                url = null,
                labelId = "$calendarId,1",
                attendees = listOf("hGL8r83T18UE,9271403242664018")
            ).runCatching {
                api.addEvent(this)
            }.onSuccess {
                eventId = it.id
                print("result: $it")
            }.onFailure {
                print(it.toString())
            }
        }
    }

    @Test
    fun test009_updateEvent() {
        runBlocking {
            val api = EventsApi(apiClient)

            UpdateEventParams(
                eventId = eventId,
                addEventsParams = AddEventsParams.AddScheduleParams(
                    calendarId = calendarId,
                    title = "UnitTestEvent updated",
                    allDay = false,
                    startAt = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }.time,
                    startTimeZone = TimeZone.getDefault(),
                    endAt = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_YEAR, 2)
                    }.time,
                    endTimeZone = TimeZone.getDefault(),
                    description = "UnitTestEvent's description updated",
                    location = "大阪",
                    url = null,
                    labelId = "hGL8r83T18UE,1",
                    attendees = listOf("hGL8r83T18UE,9271403242664018")
                )
            ).runCatching {
                api.updateEvent(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test010_addActivity() {
        runBlocking {
            val api = ActivitiesApi(apiClient)

            AddActivityParams(
                calendarId = calendarId,
                eventId = eventId,
                content = "new activity"
            ).runCatching {
                api.addActivity(this)
            }.onSuccess {
                print("result: $it")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }

    @Test
    fun test011_deleteEvent() {
        runBlocking {
            val api = EventsApi(apiClient)

            DeleteEventsParams(
                calendarId = calendarId,
                eventId = eventId
            ).runCatching {
                api.deleteEvent(this)
            }.onSuccess {
                print("deleteEvent")
            }.onFailure {
                print(it.toString())
                throw it
            }
        }
    }
}
