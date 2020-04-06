package com.engar_net.timetreeclient.api.v1.api.authorization.response

import com.engar_net.timetreeclient.model.TAccessToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TokenResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("created_at") val createdAt: Int,
    val scope: String // TODO: 仕様にないレスポンス
)

internal fun TokenResponse.toModel(): TAccessToken {
    val response = this
    return object : TAccessToken {
        override val accessToken: String get() = response.accessToken
        override val tokenType: String get() = response.tokenType
        override val scope: String get() = response.scope
        override val createdAt: Int get() = response.createdAt
    }
}