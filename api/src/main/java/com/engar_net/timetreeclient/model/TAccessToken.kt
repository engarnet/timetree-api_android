package com.engar_net.timetreeclient.model

interface TAccessToken {
    val accessToken: String
    val tokenType: String
    val scope: String
    val createdAt: Int
}