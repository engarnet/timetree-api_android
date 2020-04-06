package com.engar_net.timetreeclient.api.v1.api.authorization

import com.engar_net.timetreeclient.api.client.ApiClient
import com.engar_net.timetreeclient.api.config.Config
import com.engar_net.timetreeclient.api.v1.api.authorization.params.TokenParams
import com.engar_net.timetreeclient.api.v1.api.authorization.response.TokenResponse
import com.engar_net.timetreeclient.api.v1.api.authorization.response.toModel
import com.engar_net.timetreeclient.model.TAccessToken
import kotlinx.serialization.json.Json

class AuthorizationApi(private val apiClient: ApiClient) {
    suspend fun token(params: TokenParams): TAccessToken {
        val path = Config.tokenUrl
        val response = apiClient.post(path, params.toJson())
        print("response: $response")
        return Json.parse(TokenResponse.serializer(), response).toModel()
    }
}