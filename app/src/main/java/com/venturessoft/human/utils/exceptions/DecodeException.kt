package com.venturessoft.human.utils.exceptions

import java.lang.RuntimeException

class DecodeException: RuntimeException  {
    internal constructor(message: String?) : super(message)
    internal constructor(message: String?, cause: Throwable?) : super(message, cause)
}