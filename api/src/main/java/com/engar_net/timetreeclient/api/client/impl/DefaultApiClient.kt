package com.engar_net.timetreeclient.api.client.impl

import com.engar_net.timetreeclient.api.client.ApiClient
import com.engar_net.timetreeclient.api.config.Config
import com.engar_net.timetreeclient.api.v1.entity.ErrorEntity
import com.engar_net.timetreeclient.api.v1.exception.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.*
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DefaultApiClient : ApiClient {
    override lateinit var accessToken: String
    override val headers: Map<String, String> by lazy {
        Config.headers + if (::accessToken.isInitialized) {
            mapOf("Authorization" to "Bearer $accessToken")
        } else {
            mapOf()
        }
    }

    override suspend fun get(path: String, params: Map<String, Any>): String =
        withContext(Dispatchers.IO) {
            val queryParams = params.map { "${it.key}=${it.value}" }.joinToString("&")
            val url = URL("$path?$queryParams")
            val connection = url.openConnection() as HttpsURLConnection
            connection.requestMethod = "GET"
            connect(connection)
        }

    override suspend fun post(path: String, body: String): String = withContext(Dispatchers.IO) {
        val url = URL(path)
        val connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true
        connect(connection, body)
    }

    override suspend fun put(path: String, body: String): String = withContext(Dispatchers.IO) {
        val url = URL(path)
        val connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = "PUT"
        connection.doOutput = true
        connect(connection, body)
    }

    override suspend fun delete(path: String): String = withContext(Dispatchers.IO) {
        val url = URL(path)
        val connection = url.openConnection() as HttpsURLConnection
        connection.requestMethod = "DELETE"
        connect(connection)
    }

    private suspend fun connect(connection: HttpsURLConnection, body: String? = null): String =
        withContext(Dispatchers.IO) {
            connection.instanceFollowRedirects = false
            headers.forEach { entry ->
                connection.setRequestProperty(entry.key, entry.value)
            }

            val json: String
            try {
                connection.connect()

                body?.let {
                    print("body: $it")
                    PrintStream(connection.outputStream).print(it)
                }

                connection.handleError()
                json = BufferedInputStream(connection.inputStream).toJson()
            } finally {
                connection.disconnect()
            }
            json
        }

    private fun HttpsURLConnection.handleError() {
        // 正常終了
        if (responseCode in 200..299) return

        // TODO: 例外処理は簡易実装
        when (responseCode) {
            400 -> throw BadRequestException(errorStream.toErrorEntity())
            401 -> throw UnauthorizedException(errorStream.toErrorEntity())
            403 -> throw ForbiddenException(errorStream.toErrorEntity())
            404 -> throw NotFoundException(errorStream.toErrorEntity())
            406 -> throw NotAcceptableException(errorStream.toErrorEntity())
            429 -> throw TooManyRequestException(errorStream.toErrorEntity())
            500 -> throw InternalServerErrorException(errorStream.toErrorEntity())
            503 -> throw ServiceUnavailableException(errorStream.toErrorEntity())
            504 -> throw TimeoutException(errorStream.toErrorEntity())
            else -> throw IllegalStateException("unknown error")
        }
    }

    private fun InputStream.toJson(): String {
        val br = BufferedReader(InputStreamReader(this))
        val sb = StringBuilder()

        while (true) {
            val line = br.readLine() ?: break
            sb.append(line)
        }
        br.close()
        return sb.toString()
    }

    private fun InputStream.toErrorEntity(): ErrorEntity {
        val br = BufferedReader(InputStreamReader(this))
        val sb = StringBuilder()

        while (true) {
            val line = br.readLine() ?: break
            sb.append(line)
        }
        br.close()
        return Json.parse(ErrorEntity.serializer(), sb.toString())
    }
}