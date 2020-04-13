package com.engarnet.timetree.api.v1.api.user

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.user.response.UserResponse
import com.engarnet.timetree.api.v1.api.user.response.toModel
import com.engarnet.timetree.model.TUser
import kotlinx.serialization.json.Json

class UserApi(private val apiClient: ApiClient) {
    suspend fun user(): TUser {
        val path = Config.apiUrl + "/user"
        val response = apiClient.get(path)
        return Json.parse(UserResponse.serializer(), response).toModel()
    }
}