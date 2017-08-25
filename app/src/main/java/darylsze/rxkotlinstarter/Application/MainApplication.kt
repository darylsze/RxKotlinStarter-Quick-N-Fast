package darylsze.rxkotlinstarter.Application

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.android.autoAndroidModule
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import darylsze.rxkotlinstarter.BuildConfig
import darylsze.rxkotlinstarter.Kodein.Modules.apiModule
import darylsze.rxkotlinstarter.Kodein.Modules.networkModule
import darylsze.rxkotlinstarter.Kodein.Modules.utilModule
import darylsze.rxkotlinstarter.Retrofit.Plugins.InstantTypeConverter
import darylsze.rxkotlinstarter.Utils.CrossContextDialogManager
import darylsze.rxkotlinstarter.Utils.ProgressDialogManager
import org.joda.time.Instant
import timber.log.Timber


class MainApplication : MultiDexApplication(), KodeinAware {

    // di with context and lots of android service
    override val kodein by Kodein.lazy {
        import(autoAndroidModule(this@MainApplication))

        import(utilModule)
        import(apiModule)
        import(networkModule(applicationContext))

        bind<List<Component>>("resume") with singleton { listOf(instance<CrossContextDialogManager>(), instance<ProgressDialogManager>()) }
        bind<List<Component>>("start") with singleton { listOf<Component>() }

        bind<Gson>() with singleton {
            GsonBuilder()
                    .registerTypeAdapter(Instant::class.java, InstantTypeConverter())
                    .setLenient().create()
        }
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }
    }


    class CrashReportingTree : Timber.Tree() {
        override fun log(priority: Int, tag: String, message: String, t: Throwable) {
            when (priority) {
                Log.ERROR -> { /* record any log in error priority */ }
                Log.WARN  -> { /* record any log in warning priority */ }
            }
        }
    }
}

fun getApiDomain(): String = when (BuildConfig.FLAVOR.toUpperCase()) {
    "DEV"  -> {
        Timber.d("using debug api...")
        "http://api-dev.domain.com"
    }
    "UAT"  -> {
        Timber.d("using uat api...")
        "http://api-uat.domain.com"
    }
    "PROD" -> {
        Timber.d("using production api...")
        "http://api.domain.com"
    }
    else   -> "https://jsonplaceholder.typicode.com"
//    else   -> throw NotImplementedError("You must provide domain for build flavor ${BuildConfig.FLAVOR}")
}


interface Component {
    fun registerWithContext(context: Context)
    fun unregister()
}


