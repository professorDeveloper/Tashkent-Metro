package com.zbekz.tashkentmetro.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.zbekz.tashkentmetro.data.local.shp.AppReference
import com.zbekz.tashkentmetro.utils.ViewUtils
import com.zeugmasolutions.localehelper.LocaleAwareApplication
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
@SuppressLint("StaticFieldLeak")
class App : LocaleAwareApplication() {

    @Inject
    lateinit var userPreferenceManager: AppReference

    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(mFTActivityLifecycleCallbacks)

        ViewUtils.setLanguage(this, userPreferenceManager.language)
        ViewUtils.applyTheme(userPreferenceManager.theme)
        // ?


        //

    }


    init {
        instance = this
    }


    val mFTActivityLifecycleCallbacks = FTActivityLifecycleCallbacks()

//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//    }

    inner class FTActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        var currentActivity: Activity? = null
        override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
        override fun onActivityStarted(p0: Activity) {
            currentActivity = p0
        }

        override fun onActivityResumed(p0: Activity) {
            currentActivity = p0
        }

        override fun onActivityPaused(p0: Activity) {}
        override fun onActivityStopped(p0: Activity) {}
        override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
        override fun onActivityDestroyed(p0: Activity) {}
    }


    companion object {
        var instance: App? = null
        var context: Context? = null
        fun currentContext(): Context? {
            return instance?.mFTActivityLifecycleCallbacks?.currentActivity ?: context
        }

        fun currentActivity(): Activity? {
            return instance?.mFTActivityLifecycleCallbacks?.currentActivity
        }
    }
}