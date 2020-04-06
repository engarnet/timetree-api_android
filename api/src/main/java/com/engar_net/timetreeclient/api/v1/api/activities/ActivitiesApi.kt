package com.engar_net.timetreeclient.api.v1.api.activities

import com.engar_net.timetreeclient.api.client.ApiClient
import com.engar_net.timetreeclient.api.config.Config
import com.engar_net.timetreeclient.api.v1.api.activities.params.AddActivityParams
import com.engar_net.timetreeclient.api.v1.api.activities.response.ActivityResponse
import com.engar_net.timetreeclient.api.v1.api.activities.response.toModel
import com.engar_net.timetreeclient.model.TActivity
import kotlinx.serialization.json.Json

class ActivitiesApi(private val apiClient: ApiClient) {
    suspend fun addActivity(params: AddActivityParams): TActivity {
        val path =
            Config.apiUrl + "/calendars/" + params.calendarId + "/events/" + params.eventId + "/activities"
        val response = apiClient.post(path, params.toJson())
        print("response: $response")
        return Json.parse(ActivityResponse.serializer(), response).toModel()

    }
}