package com.bdev.hengschoolteacher.network.api_provider

interface ApiProvider<T> {
    fun provide(): T
}