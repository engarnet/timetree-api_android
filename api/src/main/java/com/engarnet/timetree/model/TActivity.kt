package com.engarnet.timetree.model

import java.util.*

interface TActivity {
    val id: String
    val content: String
    val updatedAt: Date
    val createdAt: Date
}