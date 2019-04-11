package com.esobol.Railway.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.models.NotificationAlertEntity

@Dao
interface NotificationAlertDao {

    @Query("SELECT * FROM NotificationAlertEntity")
    fun getNotificationAlerts() : List<NotificationAlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationAlert(alert: NotificationAlertEntity)
}