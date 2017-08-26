package darylsze.rxkotlinstarter.Demo

import android.app.Activity
import android.app.Application
import android.app.Service
import android.os.Bundle
import android.util.AndroidException
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.google.gson.Gson
import darylsze.rxkotlinstarter.Activity.BaseActivity
import darylsze.rxkotlinstarter.Application.MainApplication
import darylsze.rxkotlinstarter.Extension.MyCompositeDisposable
import darylsze.rxkotlinstarter.Extension.catchAllExceptions
import darylsze.rxkotlinstarter.R
import darylsze.rxkotlinstarter.Retrofit.Plugins.InstantTypeConverter
import darylsze.rxkotlinstarter.Retrofit.Service.ExampleApiService
import darylsze.rxkotlinstarter.Retrofit.Service.ExampleApiServiceImpl
import darylsze.rxkotlinstarter.Utils.CrossContextDialogManager
import darylsze.rxkotlinstarter.Utils.ProgressDialogManager
import retrofit2.Response
import retrofit2.Retrofit
import timber.log.Timber
import java.security.cert.Extension

class BasicApiCallingActivity : BaseActivity() {
    val exampleApiService by instance<ExampleApiServiceImpl>()
    val gson by instance<Gson>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exampleApiService
                .userList()
                .doOnNext { response ->
                    Timber.d(gson.toJson(response))
                }
                .doOnError { err ->
                    err.printStackTrace()
                }
                .catchAllExceptions(this)
                .subscribe()
    }
}

