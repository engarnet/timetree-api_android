package com.engarnet.timetree.api.v1.exception

import com.engarnet.timetree.api.v1.entity.ErrorEntity

class NotFoundException(val error: ErrorEntity) : Exception()