package com.engar_net.timetreeclient.api.client

interface ApiClient {
    var accessToken: String
    val headers: Map<String, String>

    suspend fun get(path: String, params: Map<String, Any> = mapOf()): String
    suspend fun post(path: String, body: String): String
    suspend fun put(path: String, body: String): String
    suspend fun delete(path: String): String
}