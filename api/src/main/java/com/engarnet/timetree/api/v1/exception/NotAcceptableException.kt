package com.engarnet.timetree.api.v1.exception

import com.engarnet.timetree.api.v1.entity.ErrorEntity

class NotAcceptableException(val error: ErrorEntity) : Exception()