package com.engar_net.timetreeclient.api.v1.api.user

import com.engar_net.timetreeclient.api.client.ApiClient
import com.engar_net.timetreeclient.api.config.Config
import com.engar_net.timetreeclient.api.v1.api.user.response.UserResponse
import com.engar_net.timetreeclient.api.v1.api.user.response.toModel
import com.engar_net.timetreeclient.model.TUser
import kotlinx.serialization.json.Json

class UserApi(private val apiClient: ApiClient) {
    suspend fun user(): TUser {
        val path = Config.apiUrl + "/user"
        val response = apiClient.get(path)
        return Json.parse(UserResponse.serializer(), response).toModel()
    }
}