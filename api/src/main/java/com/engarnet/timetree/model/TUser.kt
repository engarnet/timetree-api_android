package com.engarnet.timetree.model

import android.net.Uri

interface TUser {
    val id: String
    val name: String
    val description: String
    val imageUrl: Uri?
}