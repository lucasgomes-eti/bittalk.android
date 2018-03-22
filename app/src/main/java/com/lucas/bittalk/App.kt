package com.lucas.bittalk

import android.app.Application
import android.content.res.Configuration
import com.lucas.bittalk.helpers.Preferences
import com.onesignal.OneSignal

/**
 * Created by lucas on 28/11/2017.
 */
class App: Application() {

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onCreate() {
        super.onCreate()
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
        OneSignal.idsAvailable({ userId, _ ->
            Preferences(applicationContext).saveOneSignalUserId(userId)
        })
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}