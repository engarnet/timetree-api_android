package com.engar_net.timetreeclient.model

import android.net.Uri
import java.util.*

interface TCalendar {
    val id: String
    val name: String
    val description: String
    val color: Int
    val order: Int
    val imageUrl: Uri?
    val createdAt: Date
    val labels: List<TLabel>
    val members: List<TUser>
}