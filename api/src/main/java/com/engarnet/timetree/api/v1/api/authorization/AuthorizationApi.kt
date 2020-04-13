package com.engarnet.timetree.api.v1.api.authorization

import com.engarnet.timetree.api.client.ApiClient
import com.engarnet.timetree.api.config.Config
import com.engarnet.timetree.api.v1.api.authorization.params.TokenParams
import com.engarnet.timetree.api.v1.api.authorization.response.TokenResponse
import com.engarnet.timetree.api.v1.api.authorization.response.toModel
import com.engarnet.timetree.model.TAccessToken
import kotlinx.serialization.json.Json

class AuthorizationApi(private val apiClient: ApiClient) {
    suspend fun token(params: TokenParams): TAccessToken {
        val path = Config.tokenUrl
        val response = apiClient.post(path, params.toJson())
        print("response: $response")
        return Json.parse(TokenResponse.serializer(), response).toModel()
    }
}