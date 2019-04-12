package com.esobol.Railway.database

import android.arch.persistence.room.*
import com.esobol.Railway.models.NotificationAlert
import com.esobol.Railway.models.NotificationAlertEntity

@Dao
interface NotificationAlertDao {

    @Query("SELECT * FROM NotificationAlertEntity")
    fun getNotificationAlerts() : List<NotificationAlertEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationAlert(alert: NotificationAlertEntity)

    @Delete
    fun deleteNotificationAlert(alert: NotificationAlertEntity)
}