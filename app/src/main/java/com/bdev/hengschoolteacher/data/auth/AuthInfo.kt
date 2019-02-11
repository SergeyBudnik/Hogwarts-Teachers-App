package com.bdev.hengschoolteacher.data.auth

import java.io.Serializable

class AuthInfo(
        var token: String
) : Serializable {
    @Suppress("UNUSED")
    constructor(): this("")
}
