package com.esobol.Railway

import android.app.Activity
import android.app.Application
import android.content.Context
import com.esobol.Railway.activities.AddTicketActivity
import com.esobol.Railway.activities.TicketDetailsActivity
import com.esobol.Railway.activities.TicketListActivity
import dagger.Component
import dagger.Module
import dagger.Provides

class MyApplication: Application() {

    companion object {
        lateinit var component: InjectComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerInjectComponent
            .builder()
            .applicationContextModule(ApplicationContextModule(applicationContext))
            .build()
    }

    fun getAppContext() : Context {
        return applicationContext
    }
}

@Component(modules = arrayOf(ApplicationContextModule::class))
interface InjectComponent {

    fun inject(activity: TicketListActivity)
    fun inject(activity: AddTicketActivity)
    fun inject(activity: TicketDetailsActivity)

}


@Module
class ApplicationContextModule(val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }
}