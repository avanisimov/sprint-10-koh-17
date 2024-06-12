package ru.yandex.practicum.sprint10koh17

import java.util.Date

data class ChatItem(
    val id: String,
    val text: String,
    val date: Date,
    val origin: Origin,
    val status: Status,
) {
    enum class Origin {
        INPUT, OUTPUT
    }

    enum class Status {
        SENT, DELIVERED, READ
    }
}

