package com.esobol.Railway.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class NotificationAlertEntity() {

    @PrimaryKey
    var value: Int = 0
}

enum class NotificationAlert() {

    AT_EVENT_TIME,
    FIVE_MINUTES,
    TEN_MINUTES,
    FIFTEEN_MINUTES,
    HALF_HOUR,
    ONE_HOUR,
    TWO_HOURS
}