package com.engar_net.timetreeclient.api.v1.exception

import com.engar_net.timetreeclient.api.v1.entity.ErrorEntity

class UnauthorizedException(val error: ErrorEntity) : Exception()