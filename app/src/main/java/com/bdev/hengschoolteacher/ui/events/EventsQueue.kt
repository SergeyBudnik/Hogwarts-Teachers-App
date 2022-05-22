package com.bdev.hengschoolteacher.ui.events

class EventsQueue<T> {
    private val queue = ArrayList<T>()

    @Synchronized
    fun drainEvents(): List<T> {
        val data = queue.toList()

        queue.clear()

        return data
    }

    @Synchronized
    fun addEvent(event: T) {
        queue.add(event)
    }
}